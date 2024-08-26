package com.example.gimmegonghakauth.dao;

import com.example.gimmegonghakauth.domain.CoursesDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursesDao extends JpaRepository<CoursesDomain, Long> {

    CoursesDomain findByCourseId(Long id);

    CoursesDomain findByName(String name);

    // 띄어쓰기를 제외한 course.name 과 비교해서 반환하는 쿼리문
    @Query(value ="select * from course where REPLACE(name, ' ', '') = :name", nativeQuery = true)
    CoursesDomain findByNameIgnoreSpaces(@Param("name") String name);
}
