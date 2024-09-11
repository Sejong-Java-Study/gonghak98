package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.domain.CompletedCoursesDomain;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface CustomCompletedCoursesDao {

    @Transactional
    void saveAll(List<CompletedCoursesDomain> completedCourses);
}
