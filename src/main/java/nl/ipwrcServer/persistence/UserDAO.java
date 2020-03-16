package nl.ipwrcServer.persistence;

import nl.ipwrcServer.model.User;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import java.util.List;
import java.util.Optional;

public interface UserDAO {

    @SqlQuery("SELECT * FROM `User` INNER JOIN `Role` ON `User`.`userID` = `Role`.`roleID`")
    List<User> getAllUsers();

    @SqlQuery("SELECT `username`, `password` FROM `User` WHERE `username` = :username AND `password` = :password")
    Optional<User> findByUsernameAndPassword(@Bind("username") String username, @Bind("password") String password);

    @SqlQuery("SELECT `role` FROM `Role` INNER JOIN `User` ON `Role`.`roleID` = `User`.`userID` WHERE `username` = :username AND `password` = :password")
    List<User> checkUserRole(@BindBean User user);
}


