package com.project.server.converter;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * The TokenEncryptionUtility class provides utility methods for encryption and decryption.
 *
 * It uses the AES/ECB/PKCS5Padding encryption algorithm for this purpose.
 *
 * AES (Advanced Encryption Standard) is a symmetric encryption algorithm that provides a good balance of security and performance.
 * It is widely used in many applications and protocols.
 *
 * ECB (Electronic Codebook) is a mode of operation for block cipher algorithms like AES. In ECB mode, each block of plaintext is encrypted independently.
 * This means that identical blocks of plaintext will encrypt to identical blocks of ciphertext, which can be a security risk if the plaintext has a lot of repetition.
 * However, in this case, ECB is being used to encrypt tokens that are already random and unique. This means that the repetition issue does not apply, and ECB is a suitable choice.
 *
 * PKCS5Padding is a padding scheme used to align the input data to the block size of the encryption algorithm (16 bytes for AES).
 * The padding ensures that the input data is always a multiple of the block size.
 *
 * The combination of AES/ECB/PKCS5Padding is a common choice for encryption, providing strong security and compatibility with many systems.
 */
@Component
public class TokenEncryptionUtility {

    /**
     * Prepares and initializes a Cipher for encryption or decryption.
     *
     * @param encryptionKey the key to use for encryption or decryption
     * @param encryptionMode the mode to use for encryption or decryption
     * @return the prepared and initialized Cipher
     * @throws NoSuchPaddingException if the specified padding scheme is not available
     * @throws NoSuchAlgorithmException if the specified encryption algorithm is not available
     * @throws InvalidKeyException if the specified key is invalid
     * @throws InvalidAlgorithmParameterException if the specified algorithm parameters are invalid
     */
    public Cipher prepareAndInitCipher(String encryptionKey, int encryptionMode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        Key secretKey = new SecretKeySpec(encryptionKey.getBytes(), "AES");

        callCipherInit(cipher, encryptionMode, secretKey);
        return cipher;
    }

    /**
     * Initializes a Cipher for encryption or decryption.
     *
     * @param cipher the Cipher to initialize
     * @param encryptionMode the mode to use for encryption or decryption
     * @param secretKey the key to use for encryption or decryption
     * @throws InvalidKeyException if the specified key is invalid
     */
    void callCipherInit(Cipher cipher, int encryptionMode, Key secretKey) throws InvalidKeyException {
        cipher.init(encryptionMode, secretKey);
    }
}