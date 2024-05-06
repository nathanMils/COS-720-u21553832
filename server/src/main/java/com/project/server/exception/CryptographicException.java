package com.project.server.exception;

public class CryptographicException extends RuntimeException {
    public CryptographicException(String message) {
        super(message);
    }

    public CryptographicException(String message, Throwable cause) {
        super(message, cause);
    }
}
