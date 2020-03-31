package nl.ipwrcServer.service;

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
import java.util.EnumSet;

public class RegisterResourcesService {

    private Jdbi jdbi;
    private Environment environment;
    private WebshopConfiguration webshopConfiguration;
    private AuthenticatorService authenticatorService;
    private TokenService tokenService;
    private RegisterAccountService registerAccountService;

    private AccountDAO accountDAO;
    private UserDAO userDAO;
    private ProductDAO productDAO;

    public RegisterResourcesService(Jdbi jdbi, Environment environment, WebshopConfiguration webshopConfiguration){
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

    public void initializeVariables(){
        accountDAO = jdbi.onDemand(AccountDAO.class);
        userDAO = jdbi.onDemand(UserDAO.class);
        productDAO = jdbi.onDemand(ProductDAO.class);
        authenticatorService = new AuthenticatorService(accountDAO, webshopConfiguration);
        tokenService = new TokenService(accountDAO, webshopConfiguration);
        registerAccountService = new RegisterAccountService(accountDAO);
    }

    public void registerResources(){
        environment.jersey().register(new AccountResource(accountDAO, tokenService, registerAccountService));
        environment.jersey().register(new UserResource(userDAO));
        environment.jersey().register(new ProductResource(productDAO));
    }

    public void registerAuthentication(){
        final String PREFIX = "Bearer";
        final String REALM = "Webshop ArcadeAccount";
        environment.jersey().register(new AuthDynamicFeature(
                new OAuthJwtAndCsrfCredentialAuthFilter.Builder<Account>()
                        .setAuthenticator(authenticatorService)
                        .setAuthorizer(new AuthorizeService())
                        .setPrefix(PREFIX)
                        .setRealm(REALM)
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }

    public void registerCorsFilter(){
        final FilterRegistration.Dynamic filter = environment.servlets().addFilter("CrossOriginFilter", CrossOriginFilter.class);

        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, webshopConfiguration.getCors().getAllowedOrigins());
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, webshopConfiguration.getCors().getAllowedHeaders());
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, webshopConfiguration.getCors().getAllowedMethods());

        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());
    }

}
