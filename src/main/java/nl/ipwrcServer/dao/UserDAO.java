package nl.ipwrcServer.dao;

import nl.ipwrcServer.mapper.LoginMapper;
import nl.ipwrcServer.mapper.RoleMapper;
import nl.ipwrcServer.mapper.UserMapper;
import nl.ipwrcServer.model.User;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    @RegisterRowMapper(UserMapper.class)
    @SqlQuery("SELECT * FROM `User`")
    List<User> getAllUsers();

    @RegisterRowMapper(LoginMapper.class)
    @SqlQuery("SELECT `username`, `password` FROM `User` WHERE `username` = :username AND `password` = :password")
    Optional<User> findByUsernameAndPassword(@Bind("username") String username, @Bind("password") String password);

    @RegisterRowMapper(RoleMapper.class)
    @SqlQuery("SELECT `role` FROM `Role` WHERE `username` = :username AND `password` = :password")
    User checkUserRole(@BindBean User user);
}


