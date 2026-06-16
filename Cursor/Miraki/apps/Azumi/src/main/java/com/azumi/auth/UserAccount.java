package com.azumi.auth;

public record UserAccount(String username, String passwordHash, String salt, Role role) {
}
