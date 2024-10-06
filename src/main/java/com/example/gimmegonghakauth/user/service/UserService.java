package com.example.gimmegonghakauth.user.service;

import com.example.gimmegonghakauth.dao.CompletedCoursesDao;
import com.example.gimmegonghakauth.user.infrastructure.UserRepository;
import com.example.gimmegonghakauth.domain.CompletedCoursesDomain;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.user.domain.UserDomain;
import com.example.gimmegonghakauth.user.service.dto.ChangePasswordDto;
import com.example.gimmegonghakauth.user.service.dto.UserJoinDto;
import com.example.gimmegonghakauth.exception.UserNotFoundException;
import com.example.gimmegonghakauth.user.service.port.UserEncoder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CompletedCoursesDao completedCoursesDao;
    private final UserEncoder userEncoder;

    public UserDomain create(String _studentId, String password, String email,
        MajorsDomain majorsDomain, String name) {
        Long studentId = Long.parseLong(_studentId);
        UserDomain user = UserDomain.builder()
            .studentId(studentId).password(userEncoder.encode(password))
            .email(email).majorsDomain(majorsDomain).name(name)
            .build();
        userRepository.save(user);
        return user;
    }

    public UserDomain updatePassword(UserDomain user, String newPassword) {
        user.updatePassword(userEncoder.encode(newPassword));
        userRepository.save(user);
        return user;
    }

    public UserDomain getByStudentId(Long studentId) {
        return userRepository.findByStudentId(studentId)
            .orElseThrow(() -> new UserNotFoundException(studentId));
    }

    public boolean joinValidation(UserJoinDto userJoinDto, BindingResult bindingResult) {
        if (checkPassword(userJoinDto)) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return false;
        }
        if (checkStudentId(userJoinDto.getStudentId())) {
            bindingResult.rejectValue("studentId", "duplicate", "이미 등록된 학번입니다.");
            return false;
        }
        if (checkEmail(userJoinDto.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "이미 등록된 이메일입니다.");
            return false;
        }
        return true;
    }

    private boolean checkPassword(UserJoinDto userJoinDto) {
        if (!userJoinDto.getPassword1().equals(userJoinDto.getPassword2())) {
            return true;
        }
        return false;
    }

    private boolean checkStudentId(String studentId) {
        return userRepository.existsByStudentId(Long.parseLong(studentId));
    }

    private boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean withdrawal(String _studentId, String password) {
        Long studentId = Long.parseLong(_studentId);

        UserDomain user = userRepository.findByStudentId(studentId)
            .orElseThrow(() -> new UsernameNotFoundException("학번이 존재하지 않습니다."));

        if (userEncoder.matches(password, user.getPassword())) {
            List<CompletedCoursesDomain> coursesList = completedCoursesDao.findByUserDomain(user);
            if (!coursesList.isEmpty()) {
                // CompletedCourses 테이블에서 해당하는 행들을 삭제
                completedCoursesDao.deleteAllInBatch(coursesList);
            } //해당 유저를 참조하는 CompletedCourses 테이블 먼저 삭제
            userRepository.delete(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean changePasswordValidation(ChangePasswordDto changePasswordDto,
        BindingResult bindingResult, UserDomain user) {
        String password = user.getPassword();
        String inputPassword = changePasswordDto.getCurrentPassword();
        if (!userEncoder.matches(inputPassword, password)) { //입력한 패스워드가 현재 패스워드와 일치하지 않을 경우
            bindingResult.rejectValue("currentPassword", "currentPasswordInCorrect",
                "현재 패스워드가 일치하지 않습니다.");
            return false;
        }
        if (userEncoder.matches(changePasswordDto.getNewPassword1(),
            password)) { //입력한 새 패스워드가 현재 패스워드와 일치하는 경우
            bindingResult.rejectValue("newPassword1", "sameCurrentPassword",
                "현재 패스워드와 다른 패스워드를 입력해주세요.");
            return false;
        }
        if (!changePasswordDto.getNewPassword1()
            .equals(changePasswordDto.getNewPassword2())) {//새 패스워드 2개의 입력이 일치하지 않는 경우
            bindingResult.rejectValue("newPassword2", "newPasswordInCorrect",
                "입력한 패스워드가 일치하지 않습니다.");
            return false;
        }
        return true;
    }
}
