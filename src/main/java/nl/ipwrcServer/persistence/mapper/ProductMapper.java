package nl.ipwrcServer.persistence.mapper;

import nl.ipwrcServer.model.Product;
import nl.ipwrcServer.model.builder.ProductBuilder;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {

    @Override
    public Product map(ResultSet resultSet, StatementContext context) throws SQLException {
        return new ProductBuilder()
                .setProductID(resultSet.getInt("productID"))
                .setProductName(resultSet.getString("productName"))
                .setProductPrice(resultSet.getFloat("productPrice"))
                .setProductDescription(resultSet.getString("productDescription"))
                .setProductStatus(resultSet.getString("productStatus"))
                .build();
    }
}
