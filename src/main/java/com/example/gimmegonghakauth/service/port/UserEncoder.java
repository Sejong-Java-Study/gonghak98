package com.example.gimmegonghakauth.service.port;

public interface UserEncoder {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
