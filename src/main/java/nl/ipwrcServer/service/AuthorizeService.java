package nl.ipwrcServer.service;

import io.dropwizard.auth.Authorizer;
import nl.ipwrcServer.model.Account;

public class AuthorizeService implements Authorizer<Account> {

    private LoggerService loggerService;

    public AuthorizeService(){
        this.loggerService = new LoggerService(AuthorizeService.class);
    }

    private boolean hasRole(String roleName, Account account){
        if (account != null){
            for(String role : account.getRoles()){
                if(roleName.equals(role)){

                    return true;
                }
            }
        }
        loggerService.getWebLogger().warn(loggerService.getNO_AUTHORITY());

        return false;
    }

    @Override
    public boolean authorize(Account account, String roleName) {

        return hasRole(roleName, account);
    }

}
