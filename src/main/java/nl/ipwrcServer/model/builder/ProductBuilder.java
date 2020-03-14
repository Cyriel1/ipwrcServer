package nl.ipwrcServer.model.builder;

import nl.ipwrcServer.model.Product;

public class ProductBuilder {

    private int productID;
    private String productName;
    private float productPrice;
    private String productDescription;
    private String productStatus;

    public ProductBuilder setProductID(int productID){
        this.productID = productID;

        return this;
    }

    public ProductBuilder setProductName(String productName){
        this.productName = productName;

        return this;
    }

    public ProductBuilder setProductPrice(float productPrice){
        this.productPrice = productPrice;

        return this;
    }

    public ProductBuilder setProductDescription(String productDescription){
        this.productDescription = productDescription;

        return this;
    }

    public ProductBuilder setProductStatus(String productStatus){
        this.productStatus = productStatus;

        return this;
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

    public Product build(){
        return new Product(this);
    }
}
