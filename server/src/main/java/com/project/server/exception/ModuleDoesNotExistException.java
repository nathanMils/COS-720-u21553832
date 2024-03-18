package com.project.server.exception;


public class ModuleDoesNotExistException extends RuntimeException {
    public ModuleDoesNotExistException(String msg) {
        super(msg);
    }
}
