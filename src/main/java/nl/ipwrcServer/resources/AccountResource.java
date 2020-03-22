package nl.ipwrcServer.resources;

import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.auth.Auth;
import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.persistence.AccountDAO;
import nl.ipwrcServer.service.AuthenticatorService;
import nl.ipwrcServer.service.JsonViewService;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/account")
public class AccountResource {

    private AccountDAO accountDAO;
    private AuthenticatorService authenticatorService;

    public AccountResource(AccountDAO accountDAO, AuthenticatorService authenticatorService){
        this.accountDAO = accountDAO;
        this.authenticatorService = authenticatorService;
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
    @JsonView(JsonViewService.Public.class)
    public String getToken(Account loginCredentials){

        return authenticatorService.receiveTokenAfterValidation(loginCredentials);
    }

    @POST
    @Path("/getRegisterCredentials")
    @Consumes({MediaType.APPLICATION_JSON})
    @JsonView(JsonViewService.Public.class)
    public void getRegisterCredentials(Account registerCredentials){

        authenticatorService.registerAccount(registerCredentials);
    }


}
