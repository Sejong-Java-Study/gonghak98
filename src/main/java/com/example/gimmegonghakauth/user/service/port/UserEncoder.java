package com.example.gimmegonghakauth.user.service.port;

public interface UserEncoder {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
