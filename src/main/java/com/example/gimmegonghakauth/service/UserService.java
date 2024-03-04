package com.example.gimmegonghakauth.service;

import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.domain.UserCreateForm;
import com.example.gimmegonghakauth.domain.UserDomain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

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

    public boolean joinValidation(UserCreateForm userCreateForm, BindingResult bindingResult){
        if(checkPassword(userCreateForm)){
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return false;
        }
        if(checkStudentId(userCreateForm.getStudentId())){
            bindingResult.rejectValue("studentId", "duplicate", "이미 등록된 학번입니다.");
            return false;
        }
        return true;
    }

    public boolean checkPassword(UserCreateForm userCreateForm){
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            return true;
        }
        return false;
    }
    public boolean checkStudentId(Long studentId){
        return userDao.existsByStudentId(studentId);
    }
}
