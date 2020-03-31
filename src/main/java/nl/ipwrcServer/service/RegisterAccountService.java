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

    public String hashPassword(String password, int logRounds){

        return  BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }

    public void registerAccount(Account registerCredentials){
        final int BCRYPT_LOG_ROUNDS = 14;
        if(validateIfUsernameIsUnique(registerCredentials)){
            String hashedPassword = hashPassword(registerCredentials.getPassword(), BCRYPT_LOG_ROUNDS);
            registerCredentials.setPassword(hashedPassword);
            accountDAO.giveAccountCustomerRole(accountDAO.registerAccount(registerCredentials));

            return;
        }
        loggerService.getWebLogger().warn(loggerService.getUSERNAME_EXIST());
    }

    public boolean validateIfUsernameIsUnique(Account registerCredentials){
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
