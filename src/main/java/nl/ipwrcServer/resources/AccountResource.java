package nl.ipwrcServer.resources;

import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.auth.Auth;
import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.persistence.AccountDAO;
import nl.ipwrcServer.service.JsonViewService;
import nl.ipwrcServer.service.RegisterAccountService;
import nl.ipwrcServer.service.TokenService;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/account")
public class AccountResource {

    private AccountDAO accountDAO;
    private TokenService tokenService;
    private RegisterAccountService registerAccountService;

    public AccountResource(AccountDAO accountDAO, TokenService tokenService, RegisterAccountService registerAccountService){
        this.accountDAO = accountDAO;
        this.tokenService = tokenService;
        this.registerAccountService = registerAccountService;
    }

    @GET
    @Path("/getAll")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"ADMIN", "KLANT"})
    @JsonView(JsonViewService.Protected.class)
    public List<Account> getAll(@Auth Account account){

        return accountDAO.getAllAccounts();
    }

    @POST
    @Path("/getCredentials")
    @Consumes({MediaType.APPLICATION_JSON})
    public String[] getToken(Account loginCredentials){

        return tokenService.receiveTokenAfterValidation(loginCredentials);
    }

    @POST
    @Path("/getRegisterCredentials")
    @Consumes({MediaType.APPLICATION_JSON})
    public void getRegisterCredentials(Account registerCredentials){

        registerAccountService.registerAccount(registerCredentials);
    }

}
