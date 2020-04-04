package nl.ipwrcServer.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadFactory;
import nl.ipwrcServer.configuration.WebshopConfiguration;
import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.persistence.AccountDAO;
import org.mindrot.jbcrypt.BCrypt;
import javax.xml.bind.DatatypeConverter;
import java.security.GeneralSecurityException;
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

    private String cipherToken(String bundleToken, KeysetHandle keysetHandle){
        try {
            Aead aead = AeadFactory.getPrimitive(keysetHandle);
            byte[] cipheredToken = aead.encrypt(bundleToken.getBytes(), null);

            return DatatypeConverter.printHexBinary(cipheredToken);
        } catch (GeneralSecurityException encryptException) {
            loggerService.getWebLogger().warn("Could not encrypt bundleToken");

            return "";
        }
    }

    public String[] receiveTokenAfterValidation(Account loginCredentials){
        try {
            Account userAccount = accountDAO.findByUsername(loginCredentials);
            if(verifyHash(loginCredentials.getPassword(), userAccount.getPassword())){

                return createBundleToken(userAccount);
            }

            return new String[]{};
        }catch (NullPointerException noValidationException){
            loggerService.getWebLogger().warn(loggerService.getFAILED_VALIDATION());

            return new String[]{};
        }
    }

    private boolean verifyHash(String password, String hashedPassword){
        try{

            return BCrypt.checkpw(password, hashedPassword);
        }catch (IllegalArgumentException exception){
            loggerService.getWebLogger().warn(loggerService.getINVALID_SALT_VERSION());

            return false;
        }
    }

    public String[] reCreateBundleTokenWithExistentAccessToken(Account userAccount, String refreshToken, String accessToken){
        try {
            RSAPrivateKey privateKey = (RSAPrivateKey) keyReaderService.getPrivateKey("src/main/resources/keys/bundle_token/private_key.der");
            String csrfToken = createCsrfToken();
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);
            String token = JWT.create()
                    .withKeyId("arcade_1")
                    .withClaim("access_token", newOrExistingAccessToken(accessToken, userAccount))
                    .withClaim("refresh_token", newOrExistingRefreshToken(refreshToken))
                    .withClaim("csrf_token", csrfToken)
                    .sign(algorithm);

            return new String[]{cipherToken(token, keyReaderService.getAedKey()), csrfToken};
        } catch (JWTCreationException exception){
            loggerService.getWebLogger().warn("FAILED TO CREATE ENCRYPTED TOKEN");

            return new String[]{};
        }
    }

    private String[] createBundleToken(Account userAccount){

        return reCreateBundleTokenWithExistentAccessToken(userAccount, null, null);
    }

    public String[] reCreateBundleTokenWithExistentRefreshToken(Account userAccount, String refreshToken){

        return reCreateBundleTokenWithExistentAccessToken(userAccount, refreshToken, null);
    }

    private String newOrExistingRefreshToken(String refreshToken){
        if(refreshToken != null){

            return refreshToken;
        }

        return createRefreshToken();
    }

    private String newOrExistingAccessToken(String accessToken, Account userAccount){
        if(accessToken != null){

            return accessToken;
        }

        return createAccessToken(userAccount);
    }

    private String createAccessToken(Account userAccount) {
        try {
            Algorithm hmac256Algorithm = Algorithm.HMAC256(webshopConfiguration.getJwt().getSignature());

            return JWT.create()
                    .withIssuer(webshopConfiguration.getJwt().getAuthor())
                    .withSubject(userAccount.getUsername())
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(amountOfMinutes(15).getTimeInMillis()))
                    .withArrayClaim("role", getRolesFromDao(userAccount).toArray(new String[0]))
                    .sign(hmac256Algorithm);
        } catch (JWTCreationException createJwtException) {
            loggerService.getWebLogger().warn(loggerService.getFAILED_TOKEN_CREATION());

            return "";
        }
    }

    private ArrayList<String> getRolesFromDao(Account account){
        List<Account> accountRolesDao = accountDAO.getAccountRoles(account);
        ArrayList<String> accountRoles = new ArrayList<>();

        for(Account accountRoleDao : accountRolesDao){

            accountRoles.add(accountRoleDao.getRole());
        }

        return accountRoles;
    }

    private String createRefreshToken(){
        try {
            Algorithm hmac256Algorithm = Algorithm.HMAC256(webshopConfiguration.getJwt().getSignature());

            return JWT.create()
                    .withIssuer(webshopConfiguration.getJwt().getAuthor())
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(amountOfHours(2).getTimeInMillis()))
                    .sign(hmac256Algorithm);
        } catch (JWTCreationException createJwtException) {
            loggerService.getWebLogger().warn(loggerService.getFAILED_TOKEN_CREATION());

            return "";
        }
    }

    private String createCsrfToken(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomToken = new byte[50];
        secureRandom.nextBytes(randomToken);

        return DatatypeConverter.printHexBinary(randomToken);
    }

    private Calendar amountOfHours(int hour){
        Calendar addHours = Calendar.getInstance();
        addHours.add(Calendar.HOUR, hour);

        return addHours;
    }

    private Calendar amountOfMinutes(int minutes){
        Calendar addMinutes = Calendar.getInstance();
        addMinutes.add(Calendar.MINUTE, minutes);

        return addMinutes;
    }
}
