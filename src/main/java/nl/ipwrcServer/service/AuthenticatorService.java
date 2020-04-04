package nl.ipwrcServer.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadFactory;
import io.dropwizard.auth.Authenticator;
import nl.ipwrcServer.configuration.WebshopConfiguration;
import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.model.Token;
import nl.ipwrcServer.persistence.AccountDAO;
import javax.xml.bind.DatatypeConverter;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

public class AuthenticatorService implements Authenticator<Token, Account> {

    private AccountDAO accountDAO;
    private WebshopConfiguration webshopConfiguration;
    private LoggerService loggerService;
    private KeyReaderService keyReaderService;
    private TokenService tokenService;
    private BlackListService blackListService;
    private static String[] authBundleAndCsrfToken;

    public AuthenticatorService(AccountDAO accountDAO, WebshopConfiguration webshopConfiguration, BlackListService blackListService){
        this.loggerService = new LoggerService(AuthenticatorService.class);
        this.keyReaderService = new KeyReaderService();
        this.tokenService = new TokenService(accountDAO, webshopConfiguration);
        this.accountDAO = accountDAO;
        this.webshopConfiguration = webshopConfiguration;
        this.blackListService = blackListService;
    }

    @Override
    public Optional<Account> authenticate(Token credentials) {
        if(blackListService.ifCredentialsInBlackList(credentials)){
            loggerService.getWebLogger().warn("Credentials are in blacklist");

            return Optional.empty();
        }

        return verifyAccessToken(decipherToken(credentials, keyReaderService.getAedKey()));
    }

    private Optional<DecodedJWT> decipherToken(Token credentials, KeysetHandle keysetHandle){
        try{
            byte[] cipheredToken = DatatypeConverter.parseHexBinary(credentials.getTokenBundle());
            Aead aead = AeadFactory.getPrimitive(keysetHandle);
            byte[] decipheredToken = aead.decrypt(cipheredToken, null);

            return verifyBundleToken(new Token(new String(decipheredToken), credentials.getCsrfToken()));
        }catch (GeneralSecurityException decipherException){
            loggerService.getWebLogger().warn("Failed to decipher bundle token");
            blackListService.putInBlackListCache(credentials);

            return Optional.empty();
        }
    }

    private Optional<DecodedJWT> verifyBundleToken(Token credentials){
        RSAPublicKey publicKey = (RSAPublicKey) keyReaderService.getPublicKey("src/main/resources/keys/bundle_token/public_key.der");
        try {
            Algorithm rsa256Algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifiesBundleToken = JWT.require(rsa256Algorithm)
                    .withClaim("csrf_token", credentials.getCsrfToken())
                    .build();

            return Optional.of(verifiesBundleToken.verify(credentials.getTokenBundle()));
        } catch (JWTVerificationException verifyBundleTokenException){
            loggerService.getWebLogger().warn("Failed to verify bundle token");
            blackListService.putInBlackListCache(credentials);

            return Optional.empty();
        }
    }

    private Optional<Account> verifyAccessToken(Optional<DecodedJWT> credentials){
        try {
            Algorithm hmac256Algorithm = Algorithm.HMAC256(webshopConfiguration.getJwt().getSignature());
            JWTVerifier verifiesAccessToken = JWT.require(hmac256Algorithm)
                    .withIssuer(webshopConfiguration.getJwt().getAuthor())
                    .acceptExpiresAt(5L)
                    .build();

            if(credentials.isPresent()){
                DecodedJWT verifiedAccessToken = verifiesAccessToken.verify(credentials.get().getClaim("access_token").asString());
                String existentRefreshToken = credentials.get().getClaim("refresh_token").asString();

                return checkIfUsernameInTokenIsValid(verifiedAccessToken, existentRefreshToken);
            }

            return Optional.empty();
        } catch (JWTVerificationException verifyAccessTokenException){
            loggerService.getWebLogger().warn(loggerService.getINVALID_TOKEN_SIGNATURE());

            return decodeAccessToken(credentials);
        }
    }

    private Optional<Account> decodeAccessToken(Optional<DecodedJWT> credentials){
        try {
            if(credentials.isPresent()){
                DecodedJWT accessToken = JWT.decode(credentials.get().getClaim("access_token").asString());

                return verifyRefreshToken(credentials, accessToken);
            }

            return Optional.empty();
        } catch (JWTDecodeException decodeAccessTokenException){
            putNotVerifiedTokenInBlackList(credentials);
            loggerService.getWebLogger().warn("Could not decode access token");

            return Optional.empty();
        }
    }

    private Optional<Account> verifyRefreshToken(Optional<DecodedJWT> credentials, DecodedJWT accessToken){
        try{
            Algorithm hmac256Algorithm = Algorithm.HMAC256(webshopConfiguration.getJwt().getSignature());
            JWTVerifier verifiesRefreshToken = JWT.require(hmac256Algorithm)
                    .withIssuer(webshopConfiguration.getJwt().getAuthor())
                    .acceptExpiresAt(5L)
                    .build();

            if(credentials.isPresent()){
                DecodedJWT verifiedRefreshToken = verifiesRefreshToken.verify(credentials.get().getClaim("refresh_token").asString());
                Account userAccount = new Account(accessToken.getSubject(), accessToken.getClaim("role").asArray(String.class));
                authBundleAndCsrfToken = tokenService.reCreateBundleTokenWithExistentRefreshToken(userAccount, verifiedRefreshToken.getToken());

                return Optional.of(userAccount);
            }

            return Optional.empty();
        }catch (JWTVerificationException verifyRefreshTokenException){
            putNotVerifiedTokenInBlackList(credentials);
            loggerService.getWebLogger().warn("Failed to verify refresh token");

            return Optional.empty();
        }
    }

    private Optional<Account> checkIfUsernameInTokenIsValid(DecodedJWT verifiedAccessToken, String existentRefreshToken){
        try{
            if(accountDAO.checkIfUsernameExist(verifiedAccessToken.getSubject()).equals(verifiedAccessToken.getSubject())){
                Account userAccount = new Account(verifiedAccessToken.getSubject(), verifiedAccessToken.getClaim("role").asArray(String.class));
                authBundleAndCsrfToken = tokenService.reCreateBundleTokenWithExistentAccessToken(userAccount, existentRefreshToken ,verifiedAccessToken.getToken());

                return Optional.of(new Account(verifiedAccessToken.getSubject(), verifiedAccessToken.getClaim("role").asArray(String.class)));
            }

            return Optional.empty();
        }catch (NullPointerException noExistentUsernameException){
            loggerService.getWebLogger().warn(loggerService.getINVALID_USERNAME_IN_TOKEN());

            return Optional.empty();
        }
    }

    public void putNotVerifiedTokenInBlackList(Optional<DecodedJWT> credentials){
        credentials.ifPresent(decodedJWT -> blackListService.putInBlackListCache(new Token(decodedJWT.getToken(), decodedJWT.getClaim("csrf_token").asString())));
    }

    public static String[] getAuthBundleAndCsrfToken(){

        return authBundleAndCsrfToken;
    }
}
