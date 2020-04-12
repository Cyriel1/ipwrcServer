package nl.ipwrcServer.service;

import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.KeysetHandle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyReaderService {

    private LoggerService loggerService;

    public KeyReaderService(){
        this.loggerService = new LoggerService(KeyReaderService.class);
    }

    private byte[] readKeyBytes(String filename){
        try{

            return Files.readAllBytes(Paths.get(filename));
        }catch (IOException keyBytes){
            loggerService.getWebLogger().warn("Unable to read key file");

            return new byte[0];
        }
    }

    private KeyFactory getRSAKeyInstance(){
        try {

            return KeyFactory.getInstance("RSA");
        }catch(NoSuchAlgorithmException rsaKeyInstance){
            loggerService.getWebLogger().warn("Unable to retrieve key instance");

            return null;
        }
    }

    public PrivateKey getPrivateKey(String filename) {
        try{
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(readKeyBytes(filename));
            KeyFactory keyfactory = getRSAKeyInstance();

            return keyfactory.generatePrivate(spec);
        }catch (InvalidKeySpecException invalidKey){
            loggerService.getWebLogger().warn("Unable to retrieve private key");

            return null;
        }
    }

    public PublicKey getPublicKey(String filename) {
        try{
            X509EncodedKeySpec spec = new X509EncodedKeySpec(readKeyBytes(filename));
            KeyFactory keyFactory = getRSAKeyInstance();

            return keyFactory.generatePublic(spec);
        }catch (InvalidKeySpecException invalidKey){
            loggerService.getWebLogger().warn("Unable to retrieve public key");

            return null;
        }
    }

    public KeysetHandle getAedKey(){
        try {

            return CleartextKeysetHandle.read(JsonKeysetReader.withFile(new File("src/main/resources/keys/encrypting/encrypting_key.json")));
        } catch (GeneralSecurityException readException) {
            loggerService.getWebLogger().warn("Failed to read Aed encrypting Json Key");
        } catch (IOException noFileFoundException) {
            loggerService.getWebLogger().warn("Failed to find file");
        }

        return null;
    }

}

