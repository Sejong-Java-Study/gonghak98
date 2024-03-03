package com.example.gimmegonghakauth.service;

import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.domain.UserDomain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDomain create(Long studentId, String password, String email,
        MajorsDomain majorsDomain, String name) {
        UserDomain user = UserDomain.builder()
            .studentId(studentId).password(passwordEncoder.encode(password))
            .email(email).majorsDomain(majorsDomain).name(name)
            .build();
        userDao.save(user);
        return user;
    }
}
