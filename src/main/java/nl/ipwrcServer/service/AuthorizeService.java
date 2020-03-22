package nl.ipwrcServer.service;

import io.dropwizard.auth.Authorizer;
import nl.ipwrcServer.model.Account;

public class AuthorizeService implements Authorizer<Account> {

    private boolean hasRole(String roleName, Account account)
    {
        if (account != null)
        {
            for(String role : account.getRoles())
            {
                if(roleName.equals(role))
                {

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean authorize(Account account, String roleName) {

        return hasRole(roleName, account);
    }

}
