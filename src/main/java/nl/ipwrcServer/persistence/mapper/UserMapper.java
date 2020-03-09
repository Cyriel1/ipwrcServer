package nl.ipwrcServer.persistence.mapper;

import nl.ipwrcServer.model.User;
import nl.ipwrcServer.model.builder.UserBuilder;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    public User map(ResultSet resultSet, StatementContext context) throws SQLException {

        return new UserBuilder()
                .setUserID(resultSet.getLong("userID"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .setRole(resultSet.getString("role"))
                .build();
    }
}
