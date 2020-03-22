package nl.ipwrcServer.persistence;

import nl.ipwrcServer.model.Account;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import java.util.List;

public interface AccountDAO {

    @SqlQuery("SELECT * FROM `Account` INNER JOIN `Role` ON `Account`.`accountID` = `Role`.`roleID`")
    List<Account> getAllAccounts();

    @SqlQuery("SELECT `username` FROM `Account` WHERE `username` = :username")
    String checkIfUsernameExist(@Bind("username") String username);

    @SqlQuery("SELECT `username`, `password` FROM `Account` WHERE `username` = :username AND `password` = :password")
    Account findByUsernameAndPassword(@BindBean Account account);

    @SqlQuery("SELECT * FROM `Account` INNER JOIN WHERE `username` = :username AND `password` = :password")
    List<Account> getAccount();

    @SqlQuery("SELECT `role` FROM `Role` INNER JOIN `Account` ON `Role`.`roleID` = `Account`.`accountID` WHERE `username` = :username AND `password` = :password")
    List<Account> getAccountRoles(@BindBean Account account);

}
