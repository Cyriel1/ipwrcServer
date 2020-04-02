package nl.ipwrcServer.service;

import io.dropwizard.auth.AuthFilter;
import nl.ipwrcServer.model.Token;
import javax.annotation.Nullable;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Priority(Priorities.AUTHENTICATION)
public class OAuthCredentialsAuthFilter<P extends Principal> extends AuthFilter<Token, P> {

    private LoggerService loggerService;

    private OAuthCredentialsAuthFilter() {
        this.loggerService = new LoggerService(OAuthCredentialsAuthFilter.class);
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        String encryptedToken = getCredentials(requestContext.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

        try {
            String csrfToken = getCredentials(requestContext.getHeaders().getFirst("X-Csrf-Protection"));
            if (!authenticate(requestContext, new Token(encryptedToken, csrfToken) , SecurityContext.BASIC_AUTH)) {
                loggerService.getWebLogger().warn("USER NEEDS CREDENTIALS TO ACCESS THE RESOURCE");
            }
        }catch(WebApplicationException webApplicationException){
            unauthorizedHandler.buildResponse(prefix, realm);
        }
    }

    @Nullable
    private String getCredentials(String header) {
        if (header == null) {
            return null;
        }

        final int space = header.indexOf(' ');
        if (space <= 0) {
            return null;
        }

        return header.substring(space + 1);
    }

    public static class Builder<P extends Principal> extends AuthFilterBuilder<Token, P, OAuthCredentialsAuthFilter<P>> {

        @Override
        protected OAuthCredentialsAuthFilter<P> newInstance() {
            return new OAuthCredentialsAuthFilter<>();
        }
    }
}
