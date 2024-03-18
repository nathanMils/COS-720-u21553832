package com.project.server.exception;

public class ModuleNameAlreadyExistsException extends RuntimeException {
    public ModuleNameAlreadyExistsException(String msg) {
        super(msg);
    }
}
