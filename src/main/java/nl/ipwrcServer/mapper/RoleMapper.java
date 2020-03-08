package nl.ipwrcServer.mapper;

import nl.ipwrcServer.model.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper implements RowMapper<User> {

    public User map(ResultSet resultSet, StatementContext context) throws SQLException {
        return new User(resultSet.getString("role"));
    }
}
