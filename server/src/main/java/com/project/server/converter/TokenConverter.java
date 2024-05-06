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

import com.project.server.exception.CryptographicException;
import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * The TokenConverter class provides utility methods for encryption and decryption of tokens.
 *
 * It uses the AES/ECB/PKCS5Padding encryption algorithm, which is a common choice for encryption, providing strong security and compatibility with many systems.
 *
 * AES (Advanced Encryption Standard) is a symmetric encryption algorithm that provides a good balance of security and performance.
 * ECB (Electronic Codebook) is a mode of operation for block cipher algorithms like AES. In ECB mode, each block of plaintext is encrypted independently.
 * PKCS5Padding is a padding scheme used to align the input data to the block size of the encryption algorithm (16 bytes for AES).
 *
 * The class provides methods to prepare and initialize a Cipher for encryption or decryption, get the block size of the Cipher, and get the AlgorithmParameterSpec for the Cipher.
 */
@Converter
public class TokenConverter implements AttributeConverter<String,String> {

    private final String key;
    private final TokenEncryptionUtility encryptionUtility;

    public TokenConverter(@Value("${app.encryption.key}") String key) {
        this.key = key;
        encryptionUtility = new TokenEncryptionUtility();
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (isNotNullOrEmpty(attribute)) {
            try {
                Cipher cipher =  encryptionUtility.prepareAndInitCipher(key,Cipher.ENCRYPT_MODE);
                return encrypt(cipher, attribute);
            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
                throw new CryptographicException("Error encrypting token", e);
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
                throw new CryptographicException("Error decrypting token", e);
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
