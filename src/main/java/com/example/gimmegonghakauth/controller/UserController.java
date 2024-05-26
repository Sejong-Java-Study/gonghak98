package com.example.gimmegonghakauth.controller;

import com.example.gimmegonghakauth.dao.MajorsDao;
import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.dto.ChangePasswordDto;
import com.example.gimmegonghakauth.dto.UserJoinDto;
import com.example.gimmegonghakauth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MajorsDao majorsDao;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String userInformation(Model model, Authentication authentication){

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long studentId = Long.parseLong(userDetails.getUsername());
        UserDomain user = userDao.findByStudentId(studentId).get();
        model.addAttribute("user",user);

        return "user/information";
    }
    @GetMapping("/signup")
    public String signup(UserJoinDto userJoinDto) {
        return "user/signup";
    }


    @PostMapping("/signup")
    public String signup(@Valid UserJoinDto userJoinDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }
        if (!userService.joinValidation(userJoinDto, bindingResult)) {
            return "user/signup"; //회원가입 검증
        }
        userService.create(userJoinDto.getStudentId(), userJoinDto.getPassword1(),
            userJoinDto.getEmail(),
            majorsDao.findByMajor(userJoinDto.getMajor()), userJoinDto.getName());
        //회원 정보 저장
        return "redirect:/user/login"; //성공적인 회원가입시 로그인 페이지로 이동
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/withdrawal")
    public String withdrawal() {
        return "user/withdrawal";
    }

    @PostMapping("/withdrawal")
    public String withdrawal(@RequestParam String password, Model model,
        Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean result = userService.withdrawal(userDetails.getUsername(), password);

        if (result) {
            return "redirect:/user/logout";
        } else {
            model.addAttribute("withdrawalError", "회원 탈퇴에 실패했습니다. 비밀번호를 확인해 주세요.");
        }
        return "user/withdrawal";
    }

    @GetMapping("change/password")
    public String changePassword(ChangePasswordDto changePasswordDto) {
        return "user/changePassword";
    }

    @PostMapping("change/password")
    public String changePassword(@Valid ChangePasswordDto changePasswordDto,
        BindingResult bindingResult, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long studentId = Long.parseLong(userDetails.getUsername());
        UserDomain user = userDao.findByStudentId(studentId).get();
        if (bindingResult.hasErrors()) {
            return "user/changePassword";
        }
        if (!userService.changePasswordValidation(changePasswordDto, bindingResult, user)) {
            return "user/changePassword"; //비밀번호 변경 검증
        }
        user.updatePassword(passwordEncoder.encode(changePasswordDto.getNewPassword1()));
        userDao.save(user);
        //비밀번호 변경
        return "redirect:/user/login"; //성공적인 비밀번호 변경시 로그인 페이지로 이동
    }

    @GetMapping("/certification/clear")
    public String clearCertification() {
        return "user/resetVerify";
    }
}
