package nl.ipwrcServer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jetty.setup.ServletEnvironment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.validation.constraints.NotEmpty;
import java.util.EnumSet;

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

    public void registerCorsFilter(ServletEnvironment servletEnvironment){
        final FilterRegistration.Dynamic filter = servletEnvironment.addFilter("CrossOriginFilter", CrossOriginFilter.class);

        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, getAllowedOrigins());
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, getAllowedHeaders());
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, getAllowedMethods());

        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());
    }

}
