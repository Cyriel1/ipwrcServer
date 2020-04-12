package nl.ipwrcServer.resources;

import io.dropwizard.auth.Auth;
import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.model.Cart;
import nl.ipwrcServer.persistence.CartDAO;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/cart")
public class CartResource {

    private CartDAO cartDAO;

    public CartResource(CartDAO cartDAO){
        this.cartDAO = cartDAO;
    }

    @POST
    @Path("/sendCart")
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("KLANT")
    public String[] addCartItems(@Auth Account account, List<Cart> cart){
        cartDAO.insertUserCartInfo(account, cart);

        return new String[]{};
    }

}
