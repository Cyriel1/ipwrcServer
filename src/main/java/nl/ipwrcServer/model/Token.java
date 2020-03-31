package nl.ipwrcServer.model;

import java.util.Objects;

public class Token {

    private final String jwtToken;
    private final String csrfToken;

    public Token(String jwtToken, String csrfToken){
        this.jwtToken = Objects.requireNonNull(jwtToken);
        this.csrfToken = Objects.requireNonNull(csrfToken);
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

}
