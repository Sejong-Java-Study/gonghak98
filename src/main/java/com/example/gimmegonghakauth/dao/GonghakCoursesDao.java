package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.constant.CourseCategoryConst;
import com.example.gimmegonghakauth.domain.GonghakCoursesDomain;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto;
import com.example.gimmegonghakauth.dto.IncompletedCoursesDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GonghakCoursesDao extends JpaRepository<GonghakCoursesDomain,Long> {

    @Query("select new com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto(GCD.coursesDomain.courseId, GCD.coursesDomain.name, GCD.year, GCD.courseCategory, GCD.passCategory, GCD.designCredit, GCD.coursesDomain.credit) from GonghakCoursesDomain GCD "
        + "join CompletedCoursesDomain CCD on GCD.coursesDomain = CCD.coursesDomain "
        + "where CCD.userDomain.studentId =:studentId and GCD.majorsDomain.id = :majorsId and CCD.year = GCD.year")
    List<GonghakCoursesByMajorDto> findUserCoursesByMajorAndGonghakCoursesWithCompletedCourses(@Param("studentId") Long studentId, @Param("majorsId") Long majorId);

    @Query("select new com.example.gimmegonghakauth.dto.IncompletedCoursesDto(GCD.coursesDomain.name, GCD.courseCategory, GCD.coursesDomain.credit, GCD.designCredit) from GonghakCoursesDomain GCD  "
        + "left join CompletedCoursesDomain CCD on CCD.coursesDomain = GCD.coursesDomain "
        + "where GCD.majorsDomain = :majorsDomain and GCD.year = :year and GCD.courseCategory = :courseCategory and CCD.id is null and :studentId is not null")
    List<IncompletedCoursesDto> findUserCoursesByMajorAndCourseCategoryAndGonghakCoursesWithoutCompleteCourses(@Param("courseCategory") CourseCategoryConst courseCategory, @Param("studentId") Long studentId, @Param("majorsDomain") MajorsDomain majorsDomain, @Param("year") Long year);

}
