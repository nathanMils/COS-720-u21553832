package com.project.server.exception;

public class ModuleNotFoundException extends RuntimeException {
    public ModuleNotFoundException() {
        super("Module not found");
    }
}