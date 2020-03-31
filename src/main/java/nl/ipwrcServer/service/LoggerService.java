package nl.ipwrcServer.service;

import org.apache.log4j.Logger;

public class LoggerService {

    private final String INVALID_TOKEN_SIGNATURE = "The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA256";
    private final String INVALID_USERNAME_IN_TOKEN = "Username is not valid in token";
    private final String INVALID_SALT_VERSION = "Invalid salt version";
    private final String USERNAME_EXIST = "Username already Exist";
    private final String FAILED_VALIDATION = "Failed to validate account";
    private final String FAILED_TOKEN_CREATION = "Failed to create token";
    private final String NO_AUTHORITY = "User has no access authority";

    private Logger webLogger;

    public LoggerService(Class loggerClass){
        this.webLogger = createWebLogger(loggerClass);
    }

    public Logger createWebLogger(Class loggerClass) {
        this.webLogger = Logger.getLogger( loggerClass.getName() );

        return webLogger;
    }

    public Logger getWebLogger() {
        return webLogger;
    }

    public String getINVALID_TOKEN_SIGNATURE() {
        return INVALID_TOKEN_SIGNATURE;
    }

    public String getINVALID_USERNAME_IN_TOKEN() {
        return INVALID_USERNAME_IN_TOKEN;
    }

    public String getINVALID_SALT_VERSION() {
        return INVALID_SALT_VERSION;
    }

    public String getUSERNAME_EXIST() {
        return USERNAME_EXIST;
    }

    public String getFAILED_VALIDATION() {
        return FAILED_VALIDATION;
    }

    public String getFAILED_TOKEN_CREATION() {
        return FAILED_TOKEN_CREATION;
    }

    public String getNO_AUTHORITY() {
        return NO_AUTHORITY;
    }

}

