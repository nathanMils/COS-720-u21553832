package com.project.server.exception;

public class UnverifiedUserException extends RuntimeException{
    public UnverifiedUserException(String msg) {
        super(msg);
    }
}
