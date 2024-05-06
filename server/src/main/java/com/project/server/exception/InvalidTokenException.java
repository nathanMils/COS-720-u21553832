package com.project.server.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("INVALID_TOKEN");
    }
}
