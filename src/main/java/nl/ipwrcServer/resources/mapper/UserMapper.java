package nl.ipwrcServer.resources.mapper;

import nl.ipwrcServer.resources.model.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    public User map(ResultSet resultSet, StatementContext context) throws SQLException {
        return new User(resultSet.getLong("id"), resultSet.getString("username"), resultSet.getString("password"));
    }
}
