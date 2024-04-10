package com.example.gimmegonghakauth.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.dao.AbeekDao;
import com.example.gimmegonghakauth.dao.CompletedCoursesDao;
import com.example.gimmegonghakauth.dao.CoursesDao;
import com.example.gimmegonghakauth.dao.GonghakCorusesDao;
import com.example.gimmegonghakauth.dao.GonghakRepository;
import com.example.gimmegonghakauth.dao.MajorsDao;
import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.GonghakCoursesDomain;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.dto.GonghakRecommendCoursesDto;
import com.example.gimmegonghakauth.dto.IncompletedCoursesDto;
import com.example.testcontainer.MySqlTestContainer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
class GonghakRecommendServiceTest extends MySqlTestContainer{
    @Autowired
    private GonghakRepository gonghakRepository;
    @Autowired
    private GonghakCorusesDao gonghakCorusesDao;
    @Autowired
    private CompletedCoursesDao completedCoursesDao;
    @Autowired
    private CoursesDao coursesDao;
    @Autowired
    private AbeekDao abeekDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MajorsDao majorsDao;

    @Autowired
    private GonghakRecommendService gonghakRecommendService;

    private static final String RECOMMEND_TESTCOURSE = "testCourse5";

    @Test
    void logCreateRecommendCourses(){
        Optional<UserDomain> testUser = userDao.findById(1L);
        gonghakRecommendService.createRecommendCourses(testUser.get());
    }

    @Test
    void createRecommendCoursesTest(){
        Optional<UserDomain> testUser = userDao.findById(1L);
        Map<AbeekTypeConst, List<IncompletedCoursesDto>> recommendCoursesByAbeekType = gonghakRecommendService.createRecommendCourses(
            testUser.get()).getRecommendCoursesByAbeekType();

        assertThat(recommendCoursesByAbeekType.get(AbeekTypeConst.MSC).isEmpty()).isEqualTo(true);
        assertThat(recommendCoursesByAbeekType.get(AbeekTypeConst.PROFESSIONAL_NON_MAJOR).isEmpty()).isEqualTo(true);
        assertThat(recommendCoursesByAbeekType.get(AbeekTypeConst.MAJOR).size()).isEqualTo(1);
        assertThat(recommendCoursesByAbeekType.get(AbeekTypeConst.MAJOR).get(0).getCourseName()).isEqualTo(RECOMMEND_TESTCOURSE);
        assertThat(recommendCoursesByAbeekType.get(AbeekTypeConst.DESIGN).size()).isEqualTo(1);
        assertThat(recommendCoursesByAbeekType.get(AbeekTypeConst.DESIGN).get(0).getCourseName()).isEqualTo(RECOMMEND_TESTCOURSE);
        assertThat(recommendCoursesByAbeekType.get(AbeekTypeConst.MINIMUM_CERTI).size()).isEqualTo(1);
        assertThat(recommendCoursesByAbeekType.get(AbeekTypeConst.MINIMUM_CERTI).get(0).getCourseName()).isEqualTo(RECOMMEND_TESTCOURSE);
    }

}
