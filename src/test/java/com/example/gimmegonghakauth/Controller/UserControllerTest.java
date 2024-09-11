package com.example.gimmegonghakauth.Controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.gimmegonghakauth.dao.MajorsDao;
import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.domain.UserDomain;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
@Nested
@DisplayName("유저 컨트롤러 테스트")
@ActiveProfiles("test")
@Transactional
//@TestPropertySource(locations = "classpath:submodule-properties/application-test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MajorsDao majorsDao;

    @BeforeEach
    public void setUser() {
        String encodedPassword = new BCryptPasswordEncoder().encode("qwer");

        UserDomain user = UserDomain.builder().studentId(19111111L)
            .password(encodedPassword).email("test@sju.com")
            .majorsDomain(majorsDao.findByMajor("컴퓨터공학과"))
            .name("testUser")
            .build();
        userDao.save(user);
    }

    @Test
    @DisplayName("회원가입시 비밀번호 일치 테스트 (일치)")
    public void testSignupWithMatchingPasswords() throws Exception {
        mockMvc.perform(post("/user/signup")
                .param("studentId", "19654321")
                .param("password1", "password123")
                .param("password2", "password123")
                .param("email", "test@example.com")
                .param("major", "컴퓨터공학과")
                .param("name", "Test User")
                .with(csrf()))
            .andExpect(status().is3xxRedirection()) // 가정: 성공적인 가입은 다른 페이지로 리디렉션됨
            .andExpect(redirectedUrl("/user/login"));
    }


    @Test
    @DisplayName("회원가입시 비밀번호 일치 테스트 (불일치)")
    void testSignupWithMisMatchedPasswords() throws Exception {
        mockMvc.perform(post("/user//signup")
                .param("studentId", "19654321")
                .param("password1", "password123")
                .param("password2", "mismatchedPassword")
                .param("email", "test@example.com")
                .param("major", "컴퓨터공학과")
                .param("name", "Test User")
                .with(csrf()))
            .andExpect(MockMvcResultMatchers.view().name("user/signup"))
            .andExpect(MockMvcResultMatchers.model()
                .attributeHasFieldErrorCode("userJoinDto", "password2", "passwordInCorrect"));
        // 가정1: 불일치로 인해 다시 회원가입 폼으로 이동
        // 가정2: password2 에서 "passwordInCorrect" 오류
    }

    @Test
    @DisplayName("회원가입시 중복 학번 테스트 (불일치)")
    void testDuplicatedStudentId() throws Exception {
        mockMvc.perform(post("/user//signup")
                .param("studentId", "19111111")
                .param("password1", "password123")
                .param("password2", "password123")
                .param("email", "test@example.com")
                .param("major", "컴퓨터공학과")
                .param("name", "Test User")
                .with(csrf()))
            .andExpect(MockMvcResultMatchers.view().name("user/signup"))
            .andExpect(MockMvcResultMatchers.model()
                .attributeHasFieldErrorCode("userJoinDto", "studentId", "duplicate"));
        // 가정1: 불일치로 인해 다시 회원가입 폼으로 이동,
        // 가정2: studentId 에서 "duplicate" 오류
    }

    @Test
    @DisplayName("회원 탈퇴시 비밀번호 일치 테스트 (일치)")
    @WithMockUser(username = "19111111", password = "qwer", roles = "USER")
    public void testWithdrawalWithValidPassword() throws Exception {
        mockMvc.perform(post("/user//withdrawal")
                .param("password", "qwer")
                .with(csrf()))
            .andExpect(status().is3xxRedirection()) // 가정: 성공적인 탈퇴는 다른 페이지로 리디렉션됨
            .andExpect(redirectedUrl("/user/logout"));
    }

    @Test //(주의) DB 내용기반으로 테스트됨
    @DisplayName("회원 탈퇴시 비밀번호 일치 테스트 (불일치)")
    @WithMockUser(username = "19111111", password = "qwer", roles = "USER")
    public void testWithdrawalWithInvalidPassword() throws Exception {
        mockMvc.perform(post("/user//withdrawal")
                .param("password", "1235")
                .with(csrf()))
            .andExpect(MockMvcResultMatchers.view().name("user/withdrawal"))
            .andExpect(MockMvcResultMatchers.model()
                .attribute("withdrawalError", "회원 탈퇴에 실패했습니다. 비밀번호를 확인해 주세요."));
        //가정1 : 탈퇴가 실패하면 회원탈퇴 폼으로 이동
        //가정2 : 탈퇴가 실패하면 withdrawalError 에러 발생
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    @WithMockUser(username = "19111111", password = "qwer", roles = "USER")
    public void testChangePasswordSuccess() throws Exception {
        mockMvc.perform(post("/user/change/password")
                .param("currentPassword", "qwer")
                .param("newPassword1", "1111")
                .param("newPassword2", "1111")
                .with(csrf()))
            .andExpect(status().is3xxRedirection()) // 가정: 성공적인 변경은 다른 페이지로 리디렉션됨
            .andExpect(redirectedUrl("/user/login"));
    }

    @Test
    @DisplayName("비밀번호 변경 실패(현재 비밀번호 불일치)")
    @WithMockUser(username = "19111111", password = "qwer", roles = "USER")
    public void testChangePasswordFail1() throws Exception {
        mockMvc.perform(post("/user/change/password")
                .param("currentPassword", "1234")
                .param("newPassword1", "1235")
                .param("newPassword2", "1235")
                .with(csrf()))
            .andExpect(MockMvcResultMatchers.view().name("user/changePassword"))
            .andExpect(MockMvcResultMatchers.model()
                .attributeHasFieldErrorCode("changePasswordDto", "currentPassword",
                    "currentPasswordInCorrect"));
        //가정1 : 변경이 실패하면 비밀번호 변경 폼으로 이동
        //가정2 : 변경이 실패하면 currentPasswordInCorrect 에러 발생
    }

    @Test
    @DisplayName("비밀번호 변경 실패 (새 비밀번호와 현재 비밀번호와 일치)")
    @WithMockUser(username = "19111111", password = "qwer", roles = "USER")
    public void testChangePasswordFail2() throws Exception {
        mockMvc.perform(post("/user/change/password")
                .param("currentPassword", "qwer")
                .param("newPassword1", "qwer")
                .param("newPassword2", "qwer")
                .with(csrf()))
            .andExpect(MockMvcResultMatchers.view().name("user/changePassword"))
            .andExpect(MockMvcResultMatchers.model()
                .attributeHasFieldErrorCode("changePasswordDto", "newPassword1",
                    "sameCurrentPassword"));
        //가정1 : 변경이 실패하면 비밀번호 변경 폼으로 이동
        //가정2 : 변경이 실패하면 currentPasswordInCorrect 에러 발생
    }

    @Test
    @DisplayName("비밀번호 변경 실패(새 비밀번호 재입력 불일치)")
    @WithMockUser(username = "19111111", password = "qwer", roles = "USER")
    public void testChangePasswordFail3() throws Exception {
        mockMvc.perform(post("/user/change/password")
                .param("currentPassword", "qwer")
                .param("newPassword1", "1111")
                .param("newPassword2", "2222")
                .with(csrf()))
            .andExpect(MockMvcResultMatchers.view().name("user/changePassword"))
            .andExpect(MockMvcResultMatchers.model()
                .attributeHasFieldErrorCode("changePasswordDto", "newPassword2",
                    "newPasswordInCorrect"));
        //가정1 : 변경이 실패하면 비밀번호 변경 폼으로 이동
        //가정2 : 변경이 실패하면 currentPasswordInCorrect 에러 발생
    }

}
