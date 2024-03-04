package com.example.gimmegonghakauth.controller;

import com.example.gimmegonghakauth.dao.MajorsDao;
import com.example.gimmegonghakauth.domain.UserCreateForm;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MajorsDao majorsDao;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }


    @Transactional
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!userService.joinValidation(userCreateForm,bindingResult)) {
            return "signup_form"; //회원가입 검증
        }
        userService.create(userCreateForm.getStudentId(), userCreateForm.getPassword1(),
            userCreateForm.getEmail(),
            majorsDao.findByMajor(userCreateForm.getMajor()), userCreateForm.getName());
        //회원 정보 저장
        return "redirect:/user/signup"; //현재는 일시적으로 회원가입 페이지로 이동하도록 설정해놓음
    }
}
