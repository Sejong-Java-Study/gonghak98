package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.domain.AbeekDomain;
import com.example.gimmegonghakauth.domain.CompletedCoursesDomain;
import com.example.gimmegonghakauth.domain.CoursesDomain;
import com.example.gimmegonghakauth.domain.GonghakCoursesDomain;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.dto.GonghakCompletedCoursesDto;
import com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto;
import com.example.gimmegonghakauth.dto.GonghakStandardDto;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface GonghakRepository {

    AbeekDomain save(AbeekDomain abeekDomain);


    Optional<GonghakStandardDto> findStandard(Long studentId, MajorsDomain majorsDomain);

    Optional<GonghakCompletedCoursesDto> findUserCompletedCourses(Long studentId);

    Optional<GonghakCoursesByMajorDto> findGonghakCoursesByMajor(MajorsDomain majorsDomain);

    List<GonghakCoursesByMajorDto> findUserCoursesByMajorByGonghakCoursesWithCompletedCourses(Long studentId, MajorsDomain majorsDomain);
}
