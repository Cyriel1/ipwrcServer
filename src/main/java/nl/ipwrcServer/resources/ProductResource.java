package nl.ipwrcServer.resources;

import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.auth.Auth;
import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.model.Product;
import nl.ipwrcServer.persistence.ProductDAO;
import nl.ipwrcServer.service.ImageService;
import nl.ipwrcServer.service.JsonViewService;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/product")
public class ProductResource {

    private ProductDAO productDAO;
    private ImageService imageService;

    public ProductResource(ProductDAO productDAO, ImageService imageService){
        this.productDAO = productDAO;
        this.imageService = imageService;
    }

    @GET
    @Path("/getProducts")
    @Produces({MediaType.APPLICATION_JSON})
    @JsonView(JsonViewService.Protected.class)
    public List<Product> getAllProducts(){

        return imageService.sendProductsWithTheirImage(productDAO.getAllProducts());
    }

    @POST
    @Path("/getSelectedProducts")
    @Consumes({MediaType.APPLICATION_JSON})
    @JsonView(JsonViewService.Public.class)
    public List<Product> getSelectedProducts(int[] productIDs){
        return imageService.sendProductsWithTheirImage(productDAO.getAllSelectedProducts(productIDs));
    }

}
