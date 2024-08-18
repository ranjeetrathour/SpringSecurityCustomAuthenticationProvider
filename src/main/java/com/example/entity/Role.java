package com.example.entity;

public enum Role {
    BACHELOR("BACHELOR"),
    MASTER("MASTER"),
    DIPLOMA("DIPLOMA"),
    POST_DIPLOMA("POST_DIPLOMA");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
