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
public interface GonghakCorusesDao extends JpaRepository<GonghakCoursesDomain,Long> {

    List<GonghakCoursesDomain> findAllByMajorsDomain(MajorsDomain majorsDomain);

    @Query("select new com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto(GCD.coursesDomain.courseId, GCD.coursesDomain.name, GCD.year, GCD.courseCategory, GCD.passCategory, GCD.designCredit, GCD.coursesDomain.credit) from GonghakCoursesDomain GCD "
        + "join CompletedCoursesDomain GCCD on GCD.coursesDomain = GCCD.coursesDomain "
        + "where GCCD.userDomain.studentId =:studentId and GCD.majorsDomain.id = :majorId")
    List<GonghakCoursesByMajorDto> findUserCoursesByMajorAndGonghakCoursesWithCompletedCourses(@Param("studentId") Long studentId, @Param("majorId") Long majorId);
    // findUserCoursesByMajorAndGonghakCoursesWithCompletedCourses 메서드 실행시 학과가 달라도 같은 학수번호일시 중복해서 읽어들이는 문제가 발생
    // where 조건에 GonghakCoursesDomain의 학과ID 와 User의 학과ID가 일치하는 경우 JOIN 하도록 수정

    /*@Query("select new com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto(GCD.coursesDomain.courseId, GCD.coursesDomain.name, GCD.year, GCD.courseCategory, GCD.passCategory, GCD.designCredit, GCD.coursesDomain.credit) from GonghakCoursesDomain GCD "
        + "join CompletedCoursesDomain GCCD on GCD.coursesDomain = GCCD.coursesDomain "
        + "where GCCD.userDomain.studentId =:studentId and GCCD.userDomain.majorsDomain = :majorsDomain")
    List<GonghakCoursesByMajorDto> findUserCoursesByMajorAndGonghakCoursesWithCompletedCourses(@Param("studentId") Long studentId, @Param("majorsDomain") MajorsDomain majorsDomain);*/

    @Query("select new com.example.gimmegonghakauth.dto.IncompletedCoursesDto(GCD.coursesDomain.name, GCD.courseCategory, GCD.coursesDomain.credit, GCD.designCredit) from CompletedCoursesDomain GCCD "
        + "right outer join GonghakCoursesDomain GCD on GCCD.coursesDomain = GCD.coursesDomain "
        + "where GCD.majorsDomain = :majorsDomain and GCD.courseCategory = :courseCategory and GCCD.id is null and :studentId is not null")
    List<IncompletedCoursesDto> findUserCoursesByMajorAndCourseCategoryAndGonghakCoursesWithoutCompleteCourses(@Param("courseCategory") CourseCategoryConst courseCategory, @Param("studentId") Long studentId, @Param("majorsDomain") MajorsDomain majorsDomain);

}
