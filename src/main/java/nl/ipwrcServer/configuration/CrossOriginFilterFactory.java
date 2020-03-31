package nl.ipwrcServer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;

public class CrossOriginFilterFactory {

    @NotEmpty
    private String allowedOrigins;

    @NotEmpty
    private String allowedHeaders;

    @NotEmpty
    private String allowedMethods;

    public CrossOriginFilterFactory(){
        this.allowedOrigins = "*";
        this.allowedHeaders = "X-Requested-With,Content-Type,Accept,Origin";
        this.allowedMethods = "OPTIONS,GET,PUT,POST,DELETE,HEAD";
    }

    @JsonProperty
    public String getAllowedOrigins() {
        return allowedOrigins;
    }

    @JsonProperty
    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @JsonProperty
    public String getAllowedHeaders() {
        return allowedHeaders;
    }

    @JsonProperty
    public void setAllowedHeaders(String allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    @JsonProperty
    public String getAllowedMethods() {
        return allowedMethods;
    }

    @JsonProperty
    public void setAllowedMethods(String allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

}
