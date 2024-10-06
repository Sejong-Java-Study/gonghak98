package com.example.gimmegonghakauth.mock;

import com.example.gimmegonghakauth.user.service.port.UserEncoder;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@Primary
public class FakeUserEncoder implements UserEncoder {

    private final String PREFIX = "Fake";

    @Override
    public String encode(String rawPassword) {
        return PREFIX + rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        String password = rawPassword + PREFIX;
        if (password.equals(encodedPassword)) {
            return true;
        }
        return false;
    }
}
