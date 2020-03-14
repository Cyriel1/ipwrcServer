package nl.ipwrcServer.persistence.dao;

import nl.ipwrcServer.model.Product;
import nl.ipwrcServer.persistence.mapper.ProductMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import java.util.List;

@RegisterRowMapper(ProductMapper.class)
public interface ProductDAO {

    @SqlQuery("SELECT * FROM `Product`")
    List<Product> getAllProducts();
}
