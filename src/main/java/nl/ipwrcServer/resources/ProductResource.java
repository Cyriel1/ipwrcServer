package nl.ipwrcServer.resources;

import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.auth.Auth;
import nl.ipwrcServer.model.Product;
import nl.ipwrcServer.model.User;
import nl.ipwrcServer.persistence.dao.ProductDAO;
import nl.ipwrcServer.service.JsonViewService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/product")
public class ProductResource {

    private ProductDAO productDAO;

    public ProductResource(ProductDAO productDAO){
        this.productDAO = productDAO;
    }

    @GET
    @Path("/getProducts")
    @Produces({MediaType.APPLICATION_JSON})
    @JsonView(JsonViewService.Public.class)
    public List<Product> getAllProducts(){

        return productDAO.getAllProducts();
    }

}
