package nl.ipwrcServer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;

public class JwtTokenFactory {

    @NotEmpty
    private String author;

    @NotEmpty
    private String signature;

    @JsonProperty
    public String getAuthor() {
        return author;
    }

    @JsonProperty
    public void setAuthor(String author) {
        this.author = author;
    }

    @JsonProperty
    public String getSignature() {
        return signature;
    }

    @JsonProperty
    public void setSignature(String signature) {
        this.signature = signature;
    }

}
