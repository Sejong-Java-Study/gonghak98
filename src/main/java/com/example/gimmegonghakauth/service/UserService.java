package com.example.gimmegonghakauth.service;

import com.example.gimmegonghakauth.dao.CompletedCoursesDao;
import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.CompletedCoursesDomain;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.dto.UserJoinDto;
import com.example.gimmegonghakauth.domain.UserDomain;
import java.util.List;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class UserService {

    private final UserDao userDao;

    private final CompletedCoursesDao completedCoursesDao;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, CompletedCoursesDao completedCoursesDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.completedCoursesDao = completedCoursesDao;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDomain create(String _studentId, String password, String email,
        MajorsDomain majorsDomain, String name) {
        Long studentId = Long.parseLong(_studentId);
        UserDomain user = UserDomain.builder()
            .studentId(studentId).password(passwordEncoder.encode(password))
            .email(email).majorsDomain(majorsDomain).name(name)
            .build();
        userDao.save(user);
        return user;
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
        return true;
    }

    public boolean checkPassword(UserJoinDto userJoinDto) {
        if (!userJoinDto.getPassword1().equals(userJoinDto.getPassword2())) {
            return true;
        }
        return false;
    }

    public boolean checkStudentId(String studentId) {
        return userDao.existsByStudentId(Long.parseLong(studentId));
    }

    public boolean withdrawal(String _studentId, String password) {
        Long studentId = Long.parseLong(_studentId);

        UserDomain user = userDao.findByStudentId(studentId)
            .orElseThrow(() -> new UsernameNotFoundException("학번이 존재하지 않습니다."));

        if (passwordEncoder.matches(password, user.getPassword())) {
            List<CompletedCoursesDomain> coursesList = completedCoursesDao.findByUserDomain(user);
            if (!coursesList.isEmpty()) {
                // CompletedCourses 테이블에서 해당하는 행들을 삭제
                completedCoursesDao.deleteAllInBatch(coursesList);
            } //해당 유저를 참조하는 CompletedCourses 테이블 먼저 삭제
            userDao.delete(user);
            return true;
        } else {
            return false;
        }
    }
}
