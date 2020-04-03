package nl.ipwrcServer.service;

import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.persistence.AccountDAO;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

public class RegisterAccountService {

    private AccountDAO accountDAO;
    private LoggerService loggerService;

    public RegisterAccountService(AccountDAO accountDAO){
        this.loggerService = new LoggerService(RegisterAccountService.class);
        this.accountDAO = accountDAO;
    }

    private String hashPassword(String password){

        return  BCrypt.hashpw(password, BCrypt.gensalt(14));
    }

    public void registerAccount(Account registerCredentials){
        if(validateIfUsernameIsUnique(registerCredentials)){
            String hashedPassword = hashPassword(registerCredentials.getPassword());
            registerCredentials.setPassword(hashedPassword);
            accountDAO.giveAccountCustomerRole(accountDAO.registerAccount(registerCredentials));

            return;
        }
        loggerService.getWebLogger().warn(loggerService.getUSERNAME_EXIST());
    }

    private boolean validateIfUsernameIsUnique(Account registerCredentials){
        List<Account> allAccounts = accountDAO.getAllUsernames();
        if(allAccounts != null){
            for(Account account : allAccounts){
                if(account.getUsername().equals(registerCredentials.getUsername())){

                    return false;
                }
            }
        }

        return true;
    }

}
