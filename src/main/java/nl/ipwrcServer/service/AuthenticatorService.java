package nl.ipwrcServer.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.dropwizard.auth.Authenticator;
import nl.ipwrcServer.configuration.WebshopConfiguration;
import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.model.Token;
import nl.ipwrcServer.persistence.AccountDAO;
import java.util.*;

public class AuthenticatorService implements Authenticator<Token, Account> {

    private AccountDAO accountDAO;
    private WebshopConfiguration webshopConfiguration;
    private LoggerService loggerService;

    public AuthenticatorService(AccountDAO accountDAO, WebshopConfiguration webshopConfiguration){
        this.loggerService = new LoggerService(AuthenticatorService.class);
        this.accountDAO = accountDAO;
        this.webshopConfiguration = webshopConfiguration;
    }

    @Override
    public Optional<Account> authenticate(Token credentials) {
        return verifyToken(credentials);
    }

    public Optional<Account> verifyToken(Token credentials){
        try {
            Algorithm algorithm = Algorithm.HMAC256(webshopConfiguration.getJwt().getSignature());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(webshopConfiguration.getJwt().getAuthor())
                    .acceptExpiresAt(5L)
                    .withClaim("CsrfToken", credentials.getCsrfToken())
                    .build();
            DecodedJWT decodedJWT = verifier.verify(credentials.getJwtToken());

            return checkIfUsernameInTokenIsValid(decodedJWT);
        } catch (JWTVerificationException jwtVerificationException){
            loggerService.getWebLogger().warn(loggerService.getINVALID_TOKEN_SIGNATURE());

            return Optional.empty();
        }
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
