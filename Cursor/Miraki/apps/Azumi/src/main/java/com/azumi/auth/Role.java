package com.azumi.auth;

public enum Role {
    ADMIN("admin"),
    TEAM_MEMBER("team_member");

    private final String id;

    Role(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static Role fromId(String id) {
        for (Role role : values()) {
            if (role.id.equals(id)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + id);
    }
}
