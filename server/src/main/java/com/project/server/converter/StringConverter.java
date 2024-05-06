package com.project.server.converter;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.project.server.exception.CryptographicException;
import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * The StringConverter class is used to convert strings to and from their encrypted forms.
 * It uses the AES/GCM/NoPadding encryption algorithm for this purpose.
 *
 * AES (Advanced Encryption Standard) is a symmetric encryption algorithm that provides a good balance of security and performance.
 * It is widely used in many applications and protocols.
 *
 * GCM (Galois/Counter Mode) is a mode of operation for block cipher algorithms like AES.
 * GCM provides both data authenticity (integrity) and confidentiality. It uses a variation of the counter mode of operation for encryption,
 * and includes a hashing operation for authentication. This combination allows GCM to prevent both unauthorized data changes and eavesdropping.
 *
 * NoPadding means that the plaintext must be a multiple of the block size, and no padding is added to fill out the last block.
 * This is typically used with modes like GCM that operate on a stream of bits rather than a sequence of blocks, and therefore don't require the input to be padded to a multiple of the block size.
 *
 * The combination of AES/GCM/NoPadding is a common choice for encryption, providing strong security and compatibility with many systems.
 * The encrypted strings are still searchable because the encryption process is deterministic, i.e., the same plaintext will always encrypt to the same ciphertext,
 * as long as the same key and IV are used. This allows you to search for a string by encrypting the search term and comparing it to the encrypted strings.
 *
 * However, please note that while the strings are searchable, they are not directly readable. To read a string, you must decrypt it using the same key and IV.
 */
@Converter
public class StringConverter implements AttributeConverter<String,String> {

    private final String key;

    /**
     * Constructor for the StringConverter class.
     *
     * @param key the encryption key
     */
    public StringConverter(@Value("${app.encryption.key}") String key) {
        this.key = key;
    }

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH = 128;
    private static final int IV_LENGTH_BYTE = 12;

    /**
     * Converts the attribute to its database column representation.
     *
     * @param attribute the attribute to convert
     * @return the database column representation of the attribute
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }

        try {
            byte[] iv = generateIV();
            byte[] cipherText;
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);

            SecretKey keySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec);
            cipherText = cipher.doFinal(attribute.getBytes());

            return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new CryptographicException("Error encrypting attribute", e);
        }
    }

    /**
     * Converts the database column representation of the attribute to its original form.
     *
     * @param dbData the database column representation of the attribute
     * @return the original form of the attribute
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            String[] parts = dbData.split(":");
            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] cipherText = Base64.getDecoder().decode(parts[1]);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);

            SecretKey keySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec);
            byte[] decryptedText = cipher.doFinal(cipherText);

            return new String(decryptedText);
        } catch (Exception e) {
            throw new CryptographicException("Error decrypting attribute", e);
        }
    }

    /**
     * Generates a random initialization vector (IV).
     *
     * @return the generated IV
     */
    private byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH_BYTE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}