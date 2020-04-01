package nl.ipwrcServer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import nl.ipwrcServer.configuration.WebshopConfiguration;
import nl.ipwrcServer.service.KeyReaderService;
import nl.ipwrcServer.service.RegisterMappersService;
import nl.ipwrcServer.service.RegisterResourcesService;
import org.jdbi.v3.core.Jdbi;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class WebshopApplication extends Application<WebshopConfiguration> {

    public static void main(String[] args) throws Exception{

        KeyReaderService keyReaderService = new KeyReaderService();
        RSAPublicKey publicKey = (RSAPublicKey) keyReaderService.getPublicKey("src/main/resources/keys/public_key.der");
        RSAPrivateKey privateKey = (RSAPrivateKey) keyReaderService.getPrivateKey("src/main/resources/keys/private_key.der");
        try {
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);
            String token2 = JWT.create()
                    .withIssuer("auth0")
                    .withKeyId("arcade_1")
                    .sign(algorithm);
            System.out.println(token2);
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhdXRoMCJ9.OOXf2hnoNRJx0vx86qoBRgtgFG8IXAFI8ixBh8loUx49dKc0BH2f2fhccurRWb_WmyQ6rZx-bN1gpZrAHwlJQxSgXDfrtjiGghqDWWICLpu_UQu85fNzJcXrWNIFHLbsdTZoOm4gvBMLyoc3af__iw0SfCQ5ZwxYtfG7pJO5xv4aKq2rTxLPEEQiOcuyHCl1A0RpodZTPzl34eS07OQin-jTkj018NKJ7JkjH3OG_RERX7pLFVUlyzxtmYV25hkf1GfSUfYsdXU6MCcDKdAgRcGjRpzw0AHNyksl3sZBE1kZ3lsh02SzH_MkqlQD_GqYKkWwqSiHN_cibsC8LGR92Q";
        RSAPublicKey publicKey3 = (RSAPublicKey) keyReaderService.getPublicKey("src/main/resources/keys/public_key.der");
        RSAPrivateKey privateKey3 = (RSAPrivateKey) keyReaderService.getPrivateKey("src/main/resources/keys/private_key.der");
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey3, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            System.out.println(token);
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
        }
        new WebshopApplication().run(args);
    }

    @Override
    public void run(WebshopConfiguration webshopConfiguration, Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, webshopConfiguration.getDataSourceFactory(), "mysql");
        new RegisterResourcesService(jdbi, environment, webshopConfiguration).bundle();
        new RegisterMappersService(jdbi).registerMappersToModels();
    }

}
