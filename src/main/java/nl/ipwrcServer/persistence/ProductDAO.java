package nl.ipwrcServer.persistence;

import nl.ipwrcServer.model.Product;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import java.util.List;

public interface ProductDAO {

    @SqlQuery("SELECT * FROM `Product`")
    List<Product> getAllProducts();

}
