package com.project.server.exception;

public class ModuleNameAlreadyExists extends RuntimeException {
    public ModuleNameAlreadyExists(String msg) {
        super(msg);
    }
}
