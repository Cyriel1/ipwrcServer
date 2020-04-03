package nl.ipwrcServer.model;

import java.util.Objects;

public class Token {

    private final String tokenBundle;
    private final String csrfToken;

    public Token(String tokenBundle, String csrfToken){
        this.tokenBundle = Objects.requireNonNull(tokenBundle);
        this.csrfToken = Objects.requireNonNull(csrfToken);
    }

    public String getTokenBundle() {
        return tokenBundle;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

}
