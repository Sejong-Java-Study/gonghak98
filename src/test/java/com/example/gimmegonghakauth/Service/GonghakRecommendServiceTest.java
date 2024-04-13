package com.example.gimmegonghakauth.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.dto.IncompletedCoursesDto;
import com.example.gimmegonghakauth.dto.LoginDto;
import com.example.gimmegonghakauth.service.recommend.ComputerMajorGonghakRecommendService;
import com.example.gimmegonghakauth.service.recommend.ElecInfoMajorGonghakRecommendService;
import com.example.gimmegonghakauth.service.recommend.GonghakRecommendService;
import com.example.gimmegonghakauth.service.recommend.RecommendServiceSelectManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
class GonghakRecommendServiceTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RecommendServiceSelectManager recommendServiceSelectManager;

    @Autowired
    private ElecInfoMajorGonghakRecommendService gonghakRecommendService;

    private static final Long COM_TEST_STUDENT_ID = 19011706L;

    @Test
    @DisplayName("유저의 학과에 맞는 recommendService 클래스 가져오기 확인 - 컴퓨터공학과")
    void recommendServiceSelectManagerTest(){
        LoginDto loginDto = new LoginDto(COM_TEST_STUDENT_ID);
        GonghakRecommendService gonghakRecommendService = recommendServiceSelectManager.selectRecommendService(
            loginDto);
        assertThat(gonghakRecommendService).isInstanceOf(
            ComputerMajorGonghakRecommendService.class);
    }

    @Test
    void createRecommendCoursesTest(){
        Optional<UserDomain> testUser = userDao.findByStudentId(COM_TEST_STUDENT_ID);
        Map<AbeekTypeConst, List<IncompletedCoursesDto>> recommendCoursesByAbeekType = gonghakRecommendService.createRecommendCourses(
            testUser.get()).getRecommendCoursesByAbeekType();
        log.info("recommendCoursesByAbeekType.keySet()= {}",recommendCoursesByAbeekType.keySet());
        //[PROFESSIONAL_NON_MAJOR, BSM, DESIGN, MAJOR, MINIMUM_CERTI]
        assertThat(recommendCoursesByAbeekType.keySet()).containsOnly(
            AbeekTypeConst.PROFESSIONAL_NON_MAJOR,
            AbeekTypeConst.BSM,
            AbeekTypeConst.DESIGN,
            AbeekTypeConst.MAJOR,
            AbeekTypeConst.MINIMUM_CERTI
        );
    }
}