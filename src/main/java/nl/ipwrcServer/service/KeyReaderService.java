package nl.ipwrcServer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyReaderService {

    private LoggerService loggerService;

    public KeyReaderService(){
        this.loggerService = new LoggerService(KeyReaderService.class);
    }

    public byte[] readKeyBytes(String filename){
        try{

            return Files.readAllBytes(Paths.get(filename));
        }catch (IOException keyBytes){
            loggerService.getWebLogger().warn("UNABLE TO READ KEY FILE");

            return null;
        }
    }

    public KeyFactory getRSAKeyInstance(){
        try {

            return KeyFactory.getInstance("RSA");
        }catch(NoSuchAlgorithmException rsaKeyInstance){
            loggerService.getWebLogger().warn("UNABLE TO RETRIEVE KEY INSTANCE");

            return null;
        }
    }

    public PrivateKey getPrivateKey(String filename) {
        try{
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(readKeyBytes(filename));
            KeyFactory keyfactory = getRSAKeyInstance();

            return keyfactory.generatePrivate(spec);
        }catch (InvalidKeySpecException invalidKey){
            loggerService.getWebLogger().warn("UNABLE TO RETRIEVE PRIVATE KEY");


            return null;
        }
    }

    public PublicKey getPublicKey(String filename) {
        try{
            X509EncodedKeySpec spec = new X509EncodedKeySpec(readKeyBytes(filename));
            KeyFactory keyFactory = getRSAKeyInstance();

            return keyFactory.generatePublic(spec);
        }catch (InvalidKeySpecException invalidKey){
            loggerService.getWebLogger().warn("UNABLE TO RETRIEVE PUBLIC KEY");

            return null;
        }
    }

}
