package nl.ipwrcServer.resources.dao;

import nl.ipwrcServer.resources.mapper.UserMapper;
import nl.ipwrcServer.resources.model.User;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterRowMapper(UserMapper.class)
public interface UserDAO {

    @SqlQuery("SELECT * FROM `user`")
    List<User> getAll();
}


