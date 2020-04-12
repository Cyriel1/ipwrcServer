package nl.ipwrcServer.persistence;

import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.model.Cart;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import java.util.List;

public interface CartDAO {

    @SqlBatch("INSERT INTO `Cart` (`cartID`, `productID`, `quantity`) VALUES (:accountID , :productID, :quantity)")
    void insertUserCartInfo(@BindBean Account account, @BindBean List<Cart> cart);

}
