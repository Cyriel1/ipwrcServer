package nl.ipwrcServer.service;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Priority(Priorities.AUTHENTICATION)
public class InterceptorService implements WriterInterceptor {

    private AuthenticatorService authenticatorService;
    private LoggerService loggerService;

    public InterceptorService(AuthenticatorService authenticatorService){
        this.loggerService = new LoggerService(InterceptorService.class);
        this.authenticatorService = authenticatorService;
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
            String[] tokens = authenticatorService.getAuthBundleAndCsrfToken();
            addTokenToContent.write("[{\"tokens\":{\"refresh_token\":\"".getBytes());
            addTokenToContent.write(tokens[0].getBytes());
            addTokenToContent.write("\", \"csrf_token\":\"".getBytes());
            addTokenToContent.write(tokens[1].getBytes());
            addTokenToContent.write( "\"}, \"request\":".getBytes());
            writerInterceptorContext.setOutputStream(addTokenToContent);
            writerInterceptorContext.proceed();
            addTokenToContent.write("}]".getBytes());
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
}
