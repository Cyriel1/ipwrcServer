package nl.ipwrcServer.persistence;

import nl.ipwrcServer.model.User;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import java.util.List;

public interface UserDAO {

    @SqlQuery("SELECT * FROM `User`")
    List<User> getUsers();

}


