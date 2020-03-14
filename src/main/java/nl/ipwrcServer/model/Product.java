package nl.ipwrcServer.model;

import com.fasterxml.jackson.annotation.JsonView;
import nl.ipwrcServer.model.builder.ProductBuilder;
import nl.ipwrcServer.service.JsonViewService;
import javax.validation.constraints.NotEmpty;

public class Product {

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private int productID;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String productName;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private float productPrice;

    @JsonView(JsonViewService.Public.class)
    private String productDescription;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String productStatus;

    public Product(ProductBuilder productBuilder){
        this.productID = productBuilder.getProductID();
        this.productName = productBuilder.getProductName();
        this.productPrice = productBuilder.getProductPrice();
        this.productDescription = productBuilder.getProductDescription();
        this.productStatus = productBuilder.getProductStatus();
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductStatus() {
        return productStatus;
    }

}
