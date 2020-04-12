package nl.ipwrcServer.resources;

import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.persistence.AccountDAO;
import nl.ipwrcServer.service.RegisterAccountService;
import nl.ipwrcServer.service.TokenService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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

    @POST
    @Path("/getCredentials")
    @Consumes({MediaType.APPLICATION_JSON})
    public String[] getToken(@Valid Account loginCredentials){

        return tokenService.receiveTokenAfterValidation(loginCredentials);
    }

    @POST
    @Path("/getRegisterCredentials")
    @Consumes({MediaType.APPLICATION_JSON})
    public void getRegisterCredentials(@Valid Account registerCredentials){

        registerAccountService.registerAccount(registerCredentials);
    }

}
