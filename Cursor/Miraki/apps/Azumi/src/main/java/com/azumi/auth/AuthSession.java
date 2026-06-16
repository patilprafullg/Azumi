package com.azumi.auth;

public final class AuthSession {

    public static final String USERNAME = "azumi.username";
    public static final String ROLE = "azumi.role";

    private AuthSession() {
    }

    public static boolean isAuthenticated(jakarta.servlet.http.HttpSession session) {
        return session != null && session.getAttribute(USERNAME) != null;
    }

    public static String getUsername(jakarta.servlet.http.HttpSession session) {
        Object value = session.getAttribute(USERNAME);
        return value == null ? null : value.toString();
    }

    public static Role getRole(jakarta.servlet.http.HttpSession session) {
        Object value = session.getAttribute(ROLE);
        return value == null ? null : Role.fromId(value.toString());
    }
}
