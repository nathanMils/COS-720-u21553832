package com.project.server.request.auth;

// Validation is pointless since data isn't input into the database
public record LoginRequest(String username, String password) {
}
