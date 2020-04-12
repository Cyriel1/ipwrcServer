package nl.ipwrcServer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
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

    @JsonView(JsonViewService.Protected.class)
    private String productDescription;

    @NotEmpty
    @JsonView(JsonViewService.Protected.class)
    private String productStatus;

    @JsonIgnore
    private String productUrlImage;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String productBase64Image;

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

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductUrlImage() {
        return productUrlImage;
    }

    public void setProductUrlImage(String productUrlImage) {
        this.productUrlImage = productUrlImage;
    }

    public String getProductBase64Image() {
        return productBase64Image;
    }

    public void setProductBase64Image(String productBase64Image) {
        this.productBase64Image = productBase64Image;
    }

}
