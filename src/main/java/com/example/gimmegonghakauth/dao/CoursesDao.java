package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.domain.CoursesDomain;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursesDao extends JpaRepository<CoursesDomain, Long> {

    CoursesDomain findByCourseId(Long id);
}