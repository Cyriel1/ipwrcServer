package nl.ipwrcServer.persistence.mapper;

import nl.ipwrcServer.model.Product;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {

    @Override
    public Product map(ResultSet resultSet, StatementContext context) throws SQLException {
        return null;
    }
}
