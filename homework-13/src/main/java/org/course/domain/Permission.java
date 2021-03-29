package org.course.domain;

public enum Permission {

    BOOK_MANAGE("book:read"),
    BOOK_CREATE("book:write"),
    BOOK_UPDATE("book:write"),
    BOOK_DELETE("book:write");


    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
