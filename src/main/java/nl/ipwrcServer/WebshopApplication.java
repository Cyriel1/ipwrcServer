package nl.ipwrcServer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import nl.ipwrcServer.configuration.WebshopConfiguration;
import nl.ipwrcServer.service.RegisterMappersService;
import nl.ipwrcServer.service.RegisterResourcesService;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WebshopApplication extends Application<WebshopConfiguration> {

    public static void main(String[] args) throws Exception{
        String secretSignature = "C4h0LNGccV66798@%JC*msWeRt2000";
        String authorOfSignature = "http://arcadegamegrounds.com";
        long tokenIssuedAt = System.currentTimeMillis();
        long tokenExpiresAt = tokenIssuedAt + 765876544;
        List<String> messages = Arrays.asList("Hello", "World!", "How", "Are", "You");
        ArrayList<String> lol = new ArrayList<>();
        lol.add("het");
        String[] message = messages.toArray(new String[0]);
        System.out.println(message[4]);

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretSignature);
            String token = JWT.create()
                    .withIssuer(authorOfSignature)
                    .withSubject("Cyriel")
                    .withIssuedAt(new Date(tokenIssuedAt))
                    .withExpiresAt(new Date(tokenExpiresAt))
                    .withArrayClaim("role", new String[]{"KLANT", "ADMIN"})
                    .sign(algorithm);
            System.out.println(token);
            try {
                DecodedJWT jwt = JWT.decode(token);
                String[] test = jwt.getClaim("role").asArray(String.class);


            } catch (JWTDecodeException exception){
                //Invalid token
            }
        } catch (JWTVerificationException jwtVerificationException){

        }

        new WebshopApplication().run(args);
    }

    @Override
    public void run(WebshopConfiguration webshopConfiguration, Environment environment) throws Exception {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, webshopConfiguration.getDataSourceFactory(), "mysql");
        new RegisterResourcesService(jdbi, environment, webshopConfiguration).bundle();
        new RegisterMappersService(jdbi).registerMappersToModels();
    }

}
