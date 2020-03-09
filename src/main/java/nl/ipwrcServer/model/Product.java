package nl.ipwrcServer.model;

public class Product {

    private int productID;

    private String productName;

    private float productPrice;

    private String productDescription;

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    private String productStatus;

    public Product(Product product){
        this.productID = product.productID;
        this.productName = product.productName;
        this.productPrice = product.productPrice;
        this.productDescription = product.productDescription;
        this.productStatus = product.productStatus;
    }
}
