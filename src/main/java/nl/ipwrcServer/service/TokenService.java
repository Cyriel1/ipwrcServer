package nl.ipwrcServer.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import nl.ipwrcServer.configuration.WebshopConfiguration;
import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.persistence.AccountDAO;
import org.mindrot.jbcrypt.BCrypt;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TokenService {

    private AccountDAO accountDAO;
    private WebshopConfiguration webshopConfiguration;
    private LoggerService loggerService;
    private KeyReaderService keyReaderService;

    public TokenService(AccountDAO accountDAO, WebshopConfiguration webshopConfiguration){
        this.loggerService = new LoggerService(TokenService.class);
        this.keyReaderService = new KeyReaderService();
        this.accountDAO = accountDAO;
        this.webshopConfiguration = webshopConfiguration;
    }

    public boolean verifyHash(String password, String hashed){
        try{

            return BCrypt.checkpw(password, hashed);
        }catch (IllegalArgumentException exception){
            loggerService.getWebLogger().warn(loggerService.getINVALID_SALT_VERSION());

            return false;
        }
    }

    public String[] receiveTokenAfterValidation(Account loginCredentials){
        Account userAccount = accountDAO.findByUsername(loginCredentials);
        try {
            if(verifyHash(loginCredentials.getPassword(), userAccount.getPassword())){

                return createEncryptedToken(userAccount);
            }

            return new String[]{};
        }catch (NullPointerException exception){
            loggerService.getWebLogger().warn(loggerService.getFAILED_VALIDATION());

            return new String[]{};
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

    public String[] createEncryptedToken(Account userAccount){

        return createEncryptedToken(userAccount, null);
    }

    public String newOrExistingRefreshToken(String refreshToken){
        if(refreshToken != null){

            return refreshToken;
        }

        return createRefreshToken();
    }

    public String[] createEncryptedToken(Account userAccount, String refreshToken){
        RSAPrivateKey privateKey = (RSAPrivateKey) keyReaderService.getPrivateKey("src/main/resources/keys/private_key.der");
        String csrfToken = createCsrfToken();
        try {
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);
            String token = JWT.create()
                    .withKeyId("arcade_1")
                    .withClaim("access_token", createAccessToken(userAccount))
                    .withClaim("refresh_token", newOrExistingRefreshToken(refreshToken))
                    .withClaim("csrf_token", csrfToken)
                    .sign(algorithm);

            return new String[]{token, csrfToken};
        } catch (JWTCreationException exception){
            loggerService.getWebLogger().warn("FAILED TO CREATE ENCRYPTED TOKEN");

            return new String[]{};
        }
    }

    public String createAccessToken(Account userAccount) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(webshopConfiguration.getJwt().getSignature());
            String token = JWT.create()
                    .withIssuer(webshopConfiguration.getJwt().getAuthor())
                    .withSubject(userAccount.getUsername())
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(amountOfMinutes(1).getTimeInMillis()))
                    .withArrayClaim("role", getRolesFromDao(userAccount).toArray(new String[0]))
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException createJwtException) {
            loggerService.getWebLogger().warn(loggerService.getFAILED_TOKEN_CREATION());

            return "";
        }
    }

    public String createRefreshToken(){
        try {
            Algorithm algorithm = Algorithm.HMAC256(webshopConfiguration.getJwt().getSignature());
            String token = JWT.create()
                    .withIssuer(webshopConfiguration.getJwt().getAuthor())
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(amountOfHours(8).getTimeInMillis()))
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException createJwtException) {
            loggerService.getWebLogger().warn(loggerService.getFAILED_TOKEN_CREATION());

            return "";
        }
    }

    public String createCsrfToken(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomToken = new byte[50];
        secureRandom.nextBytes(randomToken);
        String userCsrfToken = DatatypeConverter.printHexBinary(randomToken);

        return userCsrfToken;
    }

    public Calendar amountOfHours(int hour){
        Calendar addHours = Calendar.getInstance();
        addHours.add(Calendar.HOUR, hour);

        return addHours;
    }

    public Calendar amountOfMinutes(int minutes){
        Calendar addMinutes = Calendar.getInstance();
        addMinutes.add(Calendar.MINUTE, minutes);

        return addMinutes;
    }
}
