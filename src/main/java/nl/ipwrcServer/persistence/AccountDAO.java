package nl.ipwrcServer.persistence;

import nl.ipwrcServer.model.Account;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;

public interface AccountDAO {

    @SqlQuery("SELECT * FROM `Account` INNER JOIN `Role` ON `Account`.`accountID` = `Role`.`roleID`")
    List<Account> getAllAccounts();

    @SqlQuery("SELECT `username` FROM `Account")
    List<Account> getAllUsernames();

    @SqlQuery("SELECT `username` FROM `Account` WHERE `username` = :username")
    String checkIfUsernameExist(@Bind("username") String username);

    @SqlQuery("SELECT `username`, `password` FROM `Account` WHERE `username` = :username")
    Account findByUsername(@BindBean Account account);

    @SqlUpdate("INSERT INTO `Account`(`username`, `password`) VALUES ( :username, :password )")
    @GetGeneratedKeys
    long registerAccount(@BindBean Account account);

    @SqlUpdate("INSERT INTO `Role`(`roleID`, `role`) VALUES ( :roleID, 'KLANT')")
    void giveAccountCustomerRole(@Bind("roleID") long roleID);

    @SqlQuery("SELECT `role` FROM `Role` INNER JOIN `Account` ON `Role`.`roleID` = `Account`.`accountID` WHERE `username` = :username AND `password` = :password")
    List<Account> getAccountRoles(@BindBean Account account);

    @SqlQuery("SELECT `accountID` FROM `Account` WHERE `username` = :username")
    long getAccountId(@Bind("username") String username);

}
