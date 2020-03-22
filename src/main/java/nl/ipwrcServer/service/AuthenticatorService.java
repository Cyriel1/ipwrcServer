package nl.ipwrcServer.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.dropwizard.auth.Authenticator;
import nl.ipwrcServer.configuration.WebshopConfiguration;
import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.persistence.AccountDAO;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AuthenticatorService implements Authenticator<String, Account> {

    private AccountDAO accountDAO;
    private WebshopConfiguration webshopConfiguration;

    public AuthenticatorService(AccountDAO accountDAO, WebshopConfiguration webshopConfiguration){
        this.accountDAO = accountDAO;
        this.webshopConfiguration = webshopConfiguration;
    }

    @Override
    public Optional<Account> authenticate(String credentials){

        return verifyToken(credentials);
    }

    public Optional<Account> verifyToken(String credentials){
        try {
            Algorithm algorithm = Algorithm.HMAC256(webshopConfiguration.getJwt().getSignature());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(webshopConfiguration.getJwt().getAuthor())
                    .acceptExpiresAt(5L)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(credentials);

            return checkIfUsernameInTokenIsValid(decodedJWT);
        } catch (JWTVerificationException jwtVerificationException){

            return Optional.empty();
        }
    }

    public Optional<Account> checkIfUsernameInTokenIsValid(DecodedJWT decodedJWT){
        try{
            if(accountDAO.checkIfUsernameExist(decodedJWT.getSubject()).equals(decodedJWT.getSubject())){

                return Optional.of(new Account(decodedJWT.getSubject(), decodedJWT.getClaim("role").asArray(String.class)));
            }

            return Optional.empty();
        }catch (Exception exception){

            return Optional.empty();
        }
    }

    public boolean verifyHash(String password, String hashed){

        return BCrypt.checkpw(password, hashed);
    }

    public String hashPassword(String password, int logRounds){

        return  BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }

    public void registerAccount(Account registerCredentials){
        try {
            if(validateIfUsernameIsUnique(registerCredentials)){
                String hashedPassword = hashPassword(registerCredentials.getPassword(), 11);
                registerCredentials.setPassword(hashedPassword);
                accountDAO.registerAccount(registerCredentials);
            }
        }catch (Exception exception){

        }
    }

    public boolean validateIfUsernameIsUnique(Account registerCredentials){
        List<Account> allAccounts = accountDAO.getAllUsernames();

        if(allAccounts != null){
            for(Account account : allAccounts){
                if(account.getUsername().equals(registerCredentials.getUsername())){

                    return false;
                }
            }
        }

        return true;
    }

    public String receiveTokenAfterValidation(Account loginCredentials){
        Account userAccount = accountDAO.findByUsername(loginCredentials);
        try {
            if(verifyHash(loginCredentials.getPassword(), userAccount.getPassword())){


                return createJwtToken(userAccount);
            }

            return "failed to validate account";
        }catch (Exception exception){

            return "failed to validate account";
        }
    }

    public ArrayList<String> getRolesFromDao(Account account){
        List<Account> accountRolesDao = accountDAO.getAccountRoles(account);
        ArrayList<String> accountRoles = new ArrayList<>();

        for(Account accountRoleDao : accountRolesDao){

            accountRoles.add(accountRoleDao.getRole());
        }

        return accountRoles;
    }

    public String createJwtToken(Account userAccount) {
        long tokenIssuedAt = System.currentTimeMillis();
        long tokenExpiresAt = tokenIssuedAt + calculateHoursIntoMilliSeconds(8L);
        ArrayList<String> tokenRoles = getRolesFromDao(userAccount);

        try {
            Algorithm algorithm = Algorithm.HMAC256(webshopConfiguration.getJwt().getSignature());
            String token = JWT.create()
                    .withIssuer(webshopConfiguration.getJwt().getAuthor())
                    .withSubject(userAccount.getUsername())
                    .withIssuedAt(new Date(tokenIssuedAt))
                    .withExpiresAt(new Date(tokenExpiresAt))
                    .withArrayClaim("role", tokenRoles.toArray(new String[0]))
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException createJwtException) {

            return "Failed to create token";
        }
    }

    public long calculateHoursIntoMilliSeconds(long hour){
        long minutes = 60L;
        long seconds = 60L;
        long milliSeconds = 1000L;

        return (hour * minutes * seconds * milliSeconds);
    }

}
