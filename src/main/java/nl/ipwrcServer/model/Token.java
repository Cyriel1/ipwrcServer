package nl.ipwrcServer.model;

import java.util.Objects;

public class Token {

    private final String encryptedToken;
    private final String csrfToken;

    public Token(String encryptedToken, String csrfToken){
        this.encryptedToken = Objects.requireNonNull(encryptedToken);
        this.csrfToken = Objects.requireNonNull(csrfToken);
    }

    public String getEncryptedToken() {
        return encryptedToken;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

}
