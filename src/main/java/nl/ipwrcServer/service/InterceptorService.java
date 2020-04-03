package nl.ipwrcServer.service;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

@Priority(Priorities.AUTHENTICATION)
public class InterceptorService implements WriterInterceptor {

    private AuthenticatorService authenticatorService;

    public InterceptorService(AuthenticatorService authenticatorService){
        this.authenticatorService = authenticatorService;
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext writerInterceptorContext) throws IOException, WebApplicationException {
        String[] tokens = authenticatorService.getEcryptedToken();
        OutputStream requestContent = writerInterceptorContext.getOutputStream();
        ByteArrayOutputStream addTokenToContent = new ByteArrayOutputStream();

        addTokenToContent.write("[{\"tokens\":{\"refresh_token\":\"".getBytes());
        addTokenToContent.write(tokens[0].getBytes());
        addTokenToContent.write("\", \"csrf_token\":\"".getBytes());
        addTokenToContent.write(tokens[1].getBytes());
        addTokenToContent.write( "\"}, \"request\":".getBytes());

        writerInterceptorContext.setOutputStream(addTokenToContent);
        writerInterceptorContext.proceed();

        addTokenToContent.write("}]".getBytes());
        byte[] entity = addTokenToContent.toByteArray();

        requestContent.write(entity);
        writerInterceptorContext.setOutputStream(requestContent);
    }
}
