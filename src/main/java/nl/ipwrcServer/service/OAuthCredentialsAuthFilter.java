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
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;

@Priority(Priorities.AUTHENTICATION)
public class OAuthCredentialsAuthFilter<P extends Principal> extends AuthFilter<Token, P> implements WriterInterceptor{

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
                throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix,realm));
            }
        }catch(Exception noAccessException){
            loggerService.getWebLogger().warn("USER NEEDS CREDENTIALS TO ACCESS THE RESOURCE");
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

    @Override
    public void aroundWriteTo(WriterInterceptorContext writerInterceptorContext) {
        try {
            OutputStream requestContent = writerInterceptorContext.getOutputStream();
            ByteArrayOutputStream addTokenToContent = new ByteArrayOutputStream();
            placeBundleAndCsrfTokenInOutput(writerInterceptorContext, addTokenToContent);
            writeNewOutputToRequest(writerInterceptorContext, addTokenToContent, requestContent);
        }catch (WebApplicationException webApplicationException){
            loggerService.getWebLogger().warn("Failed to output request");
        }
    }

    private void placeBundleAndCsrfTokenInOutput(WriterInterceptorContext writerInterceptorContext, ByteArrayOutputStream addTokenToContent){
        try {
            String[] tokens = AuthenticatorService.getAuthBundleAndCsrfToken();
            if(tokens != null){
                addTokenToContent.write("[{\"tokens\":{\"bundle_token\":\"".getBytes());
                addTokenToContent.write(tokens[0].getBytes());
                addTokenToContent.write("\", \"csrf_token\":\"".getBytes());
                addTokenToContent.write(tokens[1].getBytes());
                addTokenToContent.write( "\"}, \"request\":".getBytes());
                writerInterceptorContext.setOutputStream(addTokenToContent);
                writerInterceptorContext.proceed();
                addTokenToContent.write("}]".getBytes());
            }
        } catch (IOException addTokenException) {
            loggerService.getWebLogger().warn("Failed to add tokens");
        }
    }

    private void writeNewOutputToRequest(WriterInterceptorContext writerInterceptorContext, ByteArrayOutputStream addTokenToContent, OutputStream requestContent){
        try {
            byte[] entity = addTokenToContent.toByteArray();
            requestContent.write(entity);
            writerInterceptorContext.setOutputStream(requestContent);
        } catch (IOException writeOutputException) {
            loggerService.getWebLogger().warn("Failed to write new output to request");
        }
    }

    public static class Builder<P extends Principal> extends AuthFilterBuilder<Token, P, OAuthCredentialsAuthFilter<P>> {

        @Override
        protected OAuthCredentialsAuthFilter<P> newInstance() {
            return new OAuthCredentialsAuthFilter<>();
        }
    }
}
