package nl.ipwrcServer.persistence;

import nl.ipwrcServer.model.Product;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import java.util.List;

public interface ProductDAO {

    @SqlQuery("SELECT * FROM `Product`")
    List<Product> getAllProducts();

    @SqlQuery("SELECT `productID`, `productName`, `productPrice`, `productUrlImage` FROM `Product` WHERE `productID` in (<productIDs>)")
    List<Product> getAllSelectedProducts(@BindList("productIDs") int[] productIDs);

}
