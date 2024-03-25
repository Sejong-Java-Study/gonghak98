package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.domain.CompletedCoursesDomain;
import com.example.gimmegonghakauth.domain.UserDomain;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedCoursesDao extends JpaRepository<CompletedCoursesDomain, Long> {
    List<CompletedCoursesDomain> findByUserDomain(UserDomain userDomain);
}
