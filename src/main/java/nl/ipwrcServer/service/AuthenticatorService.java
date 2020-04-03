package nl.ipwrcServer.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.dropwizard.auth.Authenticator;
import nl.ipwrcServer.configuration.WebshopConfiguration;
import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.model.Token;
import nl.ipwrcServer.persistence.AccountDAO;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

public class AuthenticatorService implements Authenticator<Token, Account> {

    private AccountDAO accountDAO;
    private WebshopConfiguration webshopConfiguration;
    private LoggerService loggerService;
    private KeyReaderService keyReaderService;
    private TokenService tokenService;
    private String[] newEcryptedToken;

    public AuthenticatorService(AccountDAO accountDAO, WebshopConfiguration webshopConfiguration){
        this.loggerService = new LoggerService(AuthenticatorService.class);
        this.keyReaderService = new KeyReaderService();
        this.tokenService = new TokenService(accountDAO, webshopConfiguration);
        this.accountDAO = accountDAO;
        this.webshopConfiguration = webshopConfiguration;
    }

    @Override
    public Optional<Account> authenticate(Token credentials) {

        return verifyAccessToken(verifyEncrypting(credentials));
    }

    public DecodedJWT verifyEncrypting(Token credentials){
        RSAPublicKey publicKey = (RSAPublicKey) keyReaderService.getPublicKey("src/main/resources/keys/public_key.der");
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("csrf_token", credentials.getCsrfToken())
                    .build();
            DecodedJWT decodedJWT = verifier.verify(credentials.getEncryptedToken());

            return decodedJWT;
        } catch (JWTVerificationException exception){
            loggerService.getWebLogger().warn("FAILED VERIFYING ENCRYPTED TOKEN");

            return null;
        }
    }

    public Optional<Account> verifyAccessToken(DecodedJWT credentials){
        try {
            Algorithm algorithm = Algorithm.HMAC256(webshopConfiguration.getJwt().getSignature());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(webshopConfiguration.getJwt().getAuthor())
                    .acceptExpiresAt(5L)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(credentials.getClaim("access_token").asString());

            return checkIfUsernameInTokenIsValid(decodedJWT);
        } catch (JWTVerificationException jwtVerificationException){
            loggerService.getWebLogger().warn(loggerService.getINVALID_TOKEN_SIGNATURE());
            String token = credentials.getClaim("access_token").asString();
            try {
                DecodedJWT jwt = JWT.decode(token);

                return verifyRefreshToken(credentials, jwt);
            } catch (JWTDecodeException exception){
                loggerService.getWebLogger().warn("WAAR BEN IK IN VREDESNAAM");
                //Invalid token
            }

            return Optional.empty();
        }
    }

    public Optional<Account> verifyRefreshToken(DecodedJWT credentials, DecodedJWT accessToken){
        try{
            Algorithm algorithm = Algorithm.HMAC256(webshopConfiguration.getJwt().getSignature());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(webshopConfiguration.getJwt().getAuthor())
                    .acceptExpiresAt(5L)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(credentials.getClaim("refresh_token").asString());

            newEcryptedToken = tokenService.createEncryptedToken(new Account(accessToken.getSubject(), accessToken.getClaim("role").asArray(String.class)), credentials.getClaim("refresh_token").asString());

            return Optional.of(new Account(accessToken.getSubject(), accessToken.getClaim("role").asArray(String.class)));
        }catch (JWTVerificationException jwtVerificationException){
            loggerService.getWebLogger().warn("REFRESH TOKEN IS INVALID");

            return Optional.empty();
        }
    }

    public String[] getEcryptedToken(){

        return newEcryptedToken;
    }

    public void setEncryptedToken(String[] newEcryptedToken){
        this.newEcryptedToken = newEcryptedToken;
    }

    public Optional<Account> checkIfUsernameInTokenIsValid(DecodedJWT decodedJWT){
        try{
            if(accountDAO.checkIfUsernameExist(decodedJWT.getSubject()).equals(decodedJWT.getSubject())){

                return Optional.of(new Account(decodedJWT.getSubject(), decodedJWT.getClaim("role").asArray(String.class)));
            }

            return Optional.empty();
        }catch (NullPointerException exception){
            loggerService.getWebLogger().warn(loggerService.getINVALID_USERNAME_IN_TOKEN());

            return Optional.empty();
        }
    }

}
