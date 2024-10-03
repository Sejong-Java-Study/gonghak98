package com.example.gimmegonghakauth.Service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.service.UserService;
import com.example.gimmegonghakauth.service.port.UserEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {

    private final Long id = 10000101L;
    private final String password = "test";
    private final String email = "test@gmail.com";
    private final String name = "test_user";

    @Autowired
    private UserService userService;

    @Test
    void create로_유저를_생성할_수_있다() {
        //givne, when
        UserDomain user = userService.create(String.valueOf(id), password, email, null, name);

        //then
        assertThat(user.getId()).isNotNull();
    }

    @Test
    void updatePassword로_유저의_비밀번호를_변경할_수_있다() {
        //given
        UserDomain user = userService.create(String.valueOf(id), password, email, null, name);
        String newPassword = "test123";

        //when
        UserDomain updatedUser = userService.updatePassword(user, newPassword);

        //then
        assertThat(updatedUser.getPassword()).isEqualTo("Fake" + newPassword);
    }
}
