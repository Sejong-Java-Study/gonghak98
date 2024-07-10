package com.example.gimmegonghakauth.service;

import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.domain.UserRole;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class UserSecurityService implements UserDetailsService {

    private final UserDao userDao;

    @Value("${admin1}")
    private Long admin1;

    @Value("${admin2}")
    private Long admin2;

    @Value("${admin3}")
    private Long admin3;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDomain siteUser = validateUsername(username);
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (checkAdmin(siteUser.getStudentId())) {
                authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
            } else {
                authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
            }
            return new User(Long.toString(siteUser.getStudentId()), siteUser.getPassword(),
                authorities);
        } catch (IllegalArgumentException | UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e); // 발생한 예외를 적절히 처리하여 다시 던짐
        }
    }


    public UserDomain validateUsername(String username)
        throws IllegalArgumentException, UsernameNotFoundException {
        // username 이 null 이라면 exception
        if (username == null) {
            throw new IllegalArgumentException("학번을 입력해주세요");
        }

        try {
            Long studentId = Long.parseLong(username);
            // parseLong 과정에서 변환할 수 없다면 exception
            Optional<UserDomain> _siteUser = this.userDao.findByStudentId(studentId);
            if (_siteUser.isEmpty()) {
                throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
            }

            return _siteUser.get(); // 사용자를 찾았을 때 UserDomain 객체 반환
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("8자리 숫자의 학번을 입력해주세요.");
        }
    }

    public Boolean checkAdmin(Long studentId) {
        
        if (studentId.equals(admin1) || studentId.equals(admin2) || studentId.equals(admin3)) {
            return true;
        } else {
            return false;
        }
    }


}