package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.constant.CourseCategoryConst;
import com.example.gimmegonghakauth.domain.AbeekDomain;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto;
import com.example.gimmegonghakauth.dto.GonghakStandardDto;
import com.example.gimmegonghakauth.dto.IncompletedCoursesDto;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface GonghakRepository {

    AbeekDomain save(AbeekDomain abeekDomain);

    Optional<GonghakStandardDto> findStandard(Long studentId, MajorsDomain majorsDomain);

    List<GonghakCoursesByMajorDto> findUserCoursesByMajorByGonghakCoursesWithCompletedCourses(Long studentId, MajorsDomain majorsDomain);

    List<IncompletedCoursesDto> findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
        CourseCategoryConst courseCategory,Long studentId, MajorsDomain majorsDomain);

}
