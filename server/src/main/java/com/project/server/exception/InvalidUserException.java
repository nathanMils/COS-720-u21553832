package com.project.server.exception;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("INVALID_CREDENTIALS");
    }
}
