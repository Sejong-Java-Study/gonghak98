package com.example.gimmegonghakauth.Controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
@Nested
@DisplayName("유저 컨트롤러 테스트")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("회원가입시 비밀번호 일치 테스트 (일치)")
    public void testSignupWithMatchingPasswords() throws Exception {
        mockMvc.perform(post("/user//signup")
            .param("studentId", "123456")
            .param("password1", "password123")
            .param("password2", "password123")
            .param("email", "test@example.com")
            .param("major", "컴퓨터공학과")
            .param("name", "Test User")
            .with(csrf()))
            .andExpect(status().is3xxRedirection()) // 가정: 성공적인 가입은 다른 페이지로 리디렉션됨
            .andExpect(MockMvcResultMatchers.redirectedUrl("/user/signup"));
    }


    @Test
    @DisplayName("회원가입시 비밀번호 일치 테스트 (불일치)")
    void testSignupWithMisMatchedPasswords() throws Exception {
        mockMvc.perform(post("/user//signup")
                .param("studentId", "123456")
                .param("password1", "password123")
                .param("password2", "mismatchedPassword")
                .param("email", "test@example.com")
                .param("major", "컴퓨터공학과")
                .param("name", "Test User")
                .with(csrf()))
            .andExpect(MockMvcResultMatchers.view().name("signup_form")); // 가정: 불일치로 인해 다시 회원가입 폼으로 이동
    }
}
