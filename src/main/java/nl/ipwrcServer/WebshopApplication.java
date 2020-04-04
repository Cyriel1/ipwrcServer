package nl.ipwrcServer;

import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetWriter;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import nl.ipwrcServer.configuration.WebshopConfiguration;
import nl.ipwrcServer.service.BlackListService;
import nl.ipwrcServer.service.RegisterMappersService;
import nl.ipwrcServer.service.RegisterResourcesService;
import org.jdbi.v3.core.Jdbi;

import java.io.File;

public class WebshopApplication extends Application<WebshopConfiguration> {

    public static void main(String[] args) throws Exception{
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
