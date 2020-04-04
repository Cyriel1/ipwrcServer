package nl.ipwrcServer.service;

import com.google.crypto.tink.aead.AeadConfig;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.setup.Environment;
import nl.ipwrcServer.configuration.WebshopConfiguration;
import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.persistence.AccountDAO;
import nl.ipwrcServer.persistence.ProductDAO;
import nl.ipwrcServer.persistence.UserDAO;
import nl.ipwrcServer.resources.AccountResource;
import nl.ipwrcServer.resources.ProductResource;
import nl.ipwrcServer.resources.UserResource;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.security.GeneralSecurityException;
import java.util.EnumSet;

public class RegisterResourcesService {

    private Jdbi jdbi;
    private Environment environment;
    private WebshopConfiguration webshopConfiguration;
    private AuthenticatorService authenticatorService;
    private TokenService tokenService;
    private RegisterAccountService registerAccountService;
    private LoggerService loggerService;

    private AccountDAO accountDAO;
    private UserDAO userDAO;
    private ProductDAO productDAO;

    public RegisterResourcesService(Jdbi jdbi, Environment environment, WebshopConfiguration webshopConfiguration){
        this.loggerService = new LoggerService(RegisterResourcesService.class);
        this.jdbi = jdbi;
        this.environment = environment;
        this.webshopConfiguration = webshopConfiguration;
    }

    public void bundle(){
        initializeVariables();
        registerResources();
        registerAuthentication();
        registerCorsFilter();
    }

    private void initializeVariables(){
        registerAed();
        accountDAO = jdbi.onDemand(AccountDAO.class);
        userDAO = jdbi.onDemand(UserDAO.class);
        productDAO = jdbi.onDemand(ProductDAO.class);
        authenticatorService = new AuthenticatorService(accountDAO, webshopConfiguration);
        tokenService = new TokenService(accountDAO, webshopConfiguration);
        registerAccountService = new RegisterAccountService(accountDAO);
    }

    private void registerAed(){
        try {
            AeadConfig.register();
        } catch (GeneralSecurityException registerException) {
            loggerService.getWebLogger().warn("Tinker Aed has not been registered");
        }
    }

    private void registerResources(){
        environment.jersey().register(new AccountResource(accountDAO, tokenService, registerAccountService));
        environment.jersey().register(new UserResource(userDAO));
        environment.jersey().register(new ProductResource(productDAO));
    }

    private void registerAuthentication(){
        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialsAuthFilter.Builder<Account>()
                        .setAuthenticator(authenticatorService)
                        .setAuthorizer(new AuthorizeService())
                        .setPrefix("Bearer")
                        .setRealm("Webshop ArcadeAccount")
                        .buildAuthFilter()));
        environment.jersey().register(new RolesAllowedDynamicFeature());
    }

    private void registerCorsFilter(){
        final FilterRegistration.Dynamic filter = environment.servlets().addFilter("CrossOriginFilter", new CrossOriginFilter());

        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, webshopConfiguration.getCors().getAllowedOrigins());
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, webshopConfiguration.getCors().getAllowedHeaders());
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, webshopConfiguration.getCors().getAllowedMethods());

        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());
    }
}
