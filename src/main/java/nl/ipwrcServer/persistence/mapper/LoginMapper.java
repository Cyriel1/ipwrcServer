package nl.ipwrcServer.persistence.mapper;

import nl.ipwrcServer.model.User;
import nl.ipwrcServer.model.builder.UserBuilder;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginMapper implements RowMapper<User> {

    public User map(ResultSet resultSet, StatementContext context) throws SQLException {
        return new UserBuilder()
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .build();
    }
}
