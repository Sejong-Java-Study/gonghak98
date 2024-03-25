package com.example.gimmegonghakauth.domain;

import lombok.Getter;
import org.apache.catalina.User;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
