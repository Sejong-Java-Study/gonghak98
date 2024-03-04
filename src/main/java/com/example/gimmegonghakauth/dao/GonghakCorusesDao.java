package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.domain.GonghakCoursesDomain;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.dto.GonghakCompletedCoursesDto;
import com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GonghakCorusesDao extends JpaRepository<GonghakCoursesDomain,Long> {

    List<GonghakCoursesDomain> findAllByMajorsDomain(MajorsDomain majorsDomain);

    @Query("select new com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto(GCD.coursesDomain.courseId, GCD.coursesDomain.name, GCD.year, GCD.courseCategory, GCD.passCategory, GCD.designCredit, GCD.coursesDomain.credit) from GonghakCoursesDomain GCD "
        + "join CompletedCoursesDomain GCCD on GCD.coursesDomain = GCCD.coursesDomain "
        + "where GCCD.userDomain.studentId =:studentId and GCCD.userDomain.majorsDomain = :majorsDomain")
    List<GonghakCoursesByMajorDto> findUserCoursesByMajorByGonghakCoursesWithCompletedCourses(@Param("studentId") Long studentId, @Param("majorsDomain") MajorsDomain majorsDomain);
}
