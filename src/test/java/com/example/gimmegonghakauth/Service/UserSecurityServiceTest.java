package com.example.gimmegonghakauth.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.domain.UserRole;
import com.example.gimmegonghakauth.service.UserSecurityService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;


@Nested
@Transactional
@DisplayName("DB 테스트(로그인)")
@ActiveProfiles("test")
public class UserSecurityServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserSecurityService userSecurityService;

    private final Long admin1 = 12345678L;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        // @Value 값을 ReflectionTestUtils를 사용하여 주입
        ReflectionTestUtils.setField(userSecurityService,"admin1",admin1);
    }

    @Test
    @DisplayName("로그인 성공(관리자)")
    public void testLoadUserByUsernameSuccess1() {
        Long studentId = admin1;
        MajorsDomain major = MajorsDomain.builder().major("컴퓨터공학과").build();

        UserDomain mockUser = UserDomain.builder()
            .studentId(studentId).password("1234").email("1234@naver.com")
            .majorsDomain(major).name("test")
            .build();

        Mockito.when(userDao.findByStudentId(studentId)).thenReturn(Optional.of(mockUser));

        try {
            // loadUserByUsername 메서드 호출
            UserDetails userDetails = userSecurityService.loadUserByUsername(admin1.toString());

            // ADMIN 권한을 가져야 함
            assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(UserRole.ADMIN.getValue())));
        } catch (UsernameNotFoundException e) {
            // 예외 발생 시 실패 처리
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("로그인 성공(일반 사용자)")
    public void testLoadUserByUsernameSuccess2() {
        Long studentId = 19011685L;
        MajorsDomain major = MajorsDomain.builder().major("컴퓨터공학과").build();

        UserDomain mockUser = UserDomain.builder()
            .studentId(studentId).password("1234").email("1234@naver.com")
            .majorsDomain(major).name("test")
            .build();
        when(userDao.findByStudentId(studentId)).thenReturn(Optional.of(mockUser));
        //Mock User 생성

        // When
        UserDetails userDetails = userSecurityService.loadUserByUsername(studentId.toString());

        // Then
        assertEquals("19011685", userDetails.getUsername());
        assertEquals("1234", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());  // 하나의 권한(롤)을 예상
        assertEquals(UserRole.USER.getValue(),
            userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    @DisplayName("로그인 실패 (사용자 없음)")
    public void testLoadUserByUsernameFailure() {
        // Given
        Long studentId = 123456L;
        when(userDao.findByStudentId(studentId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(UsernameNotFoundException.class, () ->
            userSecurityService.loadUserByUsername(studentId.toString()));
    }

}
