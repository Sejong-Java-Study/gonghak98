package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<UserDomain, Long> {

    UserDomain findByStudentId(Long studentId);

    boolean existsByStudentId(Long studentId);
}