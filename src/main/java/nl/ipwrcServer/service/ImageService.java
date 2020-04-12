package nl.ipwrcServer.service;

import nl.ipwrcServer.model.Product;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class ImageService {

    private LoggerService loggerService;

    public ImageService(){
        this.loggerService = new LoggerService(ImageService.class);
    }

    public List<Product> sendProductsWithTheirImage(List<Product> products){
        for(Product product : products){
            if(!product.getProductUrlImage().isEmpty()){
                product.setProductBase64Image(convertImageToBase64(product.getProductUrlImage()));
            }
        }

        return products;
    }

    public String convertImageToBase64(String filePath){
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));

            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException convertingException) {
            loggerService.getWebLogger().warn("Could Not Convert Image File To Base 64");

            return "";
        }
    }

}
