package com.project.server.converter;

import static io.micrometer.common.util.StringUtils.isNotEmpty;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringConverter implements AttributeConverter<String,String> {

    private final String key;
    private final EncryptionUtility encryptionUtility;

    public StringConverter(@Value("app.encryption.key") String key) {
        this(key,new EncryptionUtility());
    }

    private StringConverter(String key, EncryptionUtility encryptionUtility) {
        this.key = key;
        this.encryptionUtility = encryptionUtility;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (isNotNullOrEmpty(attribute)) {
            try {
                Cipher cipher =  encryptionUtility.prepareAndInitCipher(key,Cipher.ENCRYPT_MODE);
                return encrypt(cipher, attribute);
            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            }
        }
        return entityAttributeToString(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (isNotEmpty(dbData)) {
            try {
                Cipher cipher = encryptionUtility.prepareAndInitCipher(key,Cipher.DECRYPT_MODE);
                return decrypt(cipher, dbData);
            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            }
        }
        return stringToEntityAttribute(dbData);
    }

    private boolean isNotNullOrEmpty(String attribute) {
        return attribute != null && !attribute.matches("");
    }

    private String stringToEntityAttribute(String dbData) {
        return dbData;
    }

    private String entityAttributeToString(String attribute) {
        return attribute;
    }

    byte[] callCipherDoFinal(Cipher cipher, byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(bytes);
    }

    private String encrypt(Cipher cipher, String attribute) throws IllegalBlockSizeException, BadPaddingException {
        byte[] bytesToEncrypt = entityAttributeToString(attribute).getBytes();
        byte[] encryptedBytes = callCipherDoFinal(cipher, bytesToEncrypt);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private String decrypt(Cipher cipher, String dbData) throws IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = Base64.getDecoder().decode(dbData);
        byte[] decryptedBytes = callCipherDoFinal(cipher, encryptedBytes);
        return stringToEntityAttribute(new String(decryptedBytes));
    }
    
}
