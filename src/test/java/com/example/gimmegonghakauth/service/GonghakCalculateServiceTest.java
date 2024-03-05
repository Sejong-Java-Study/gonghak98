package com.example.gimmegonghakauth.service;

import static org.assertj.core.api.Assertions.*;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.constant.CourseCategoryConst;
import com.example.gimmegonghakauth.dao.AbeekDao;
import com.example.gimmegonghakauth.dao.CompletedCoursesDao;
import com.example.gimmegonghakauth.dao.CoursesDao;
import com.example.gimmegonghakauth.dao.GonghakCorusesDao;
import com.example.gimmegonghakauth.dao.GonghakRepository;
import com.example.gimmegonghakauth.dao.MajorsDao;
import com.example.gimmegonghakauth.dao.UserDao;
import com.example.gimmegonghakauth.domain.AbeekDomain;
import com.example.gimmegonghakauth.domain.CompletedCoursesDomain;
import com.example.gimmegonghakauth.domain.CoursesDomain;
import com.example.gimmegonghakauth.domain.GonghakCoursesDomain;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.dto.GonghakResultDto;
import com.example.gimmegonghakauth.dto.GonghakStandardDto;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class GonghakCalculateServiceTest {

    @Autowired
    private GonghakRepository gonghakRepository;
    @Autowired
    private GonghakCalculateService gonghakCalculateService;
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

    private static final MajorsDomain TEST_MAJORSDOMAIN = MajorsDomain.builder()
        .id(1L)
        .major("컴퓨터공학과").build();

    private static final UserDomain TEST_USERDOMAIN = UserDomain.builder()
        .email("testEmail")
        .name("홍지섭")
        .password("qwer")
        .studentId(19011706L)
        .majorsDomain(TEST_MAJORSDOMAIN).build();

    @Test
    @DisplayName("check log gonghakCalculateServiceTest")
    void logGonghakCalculateServiceTest(){
        Optional<UserDomain> testUser = userDao.findById(1L);

        Map<AbeekTypeConst, Double> userResultRatio = gonghakCalculateService.getResultRatio(
            testUser.get()).get().getUserResultRatio();

        log.info("-------------userResultRatio = {}",userResultRatio);
    }

    //리팩토링 필요
    @Test
    @DisplayName("correctGonghakCalculateServiceTest")
    void correctGonghakCalculateServiceTest(){
        Optional<UserDomain> testUser = userDao.findById(1L);

        //given
        CoursesDomain testCoursePlus1 = CoursesDomain.builder()
            .courseId(7777L)
            .credit(3)
            .name("testCoursePlus1").build();
        coursesDao.save(testCoursePlus1);

        CompletedCoursesDomain coursesDomain4 = CompletedCoursesDomain.builder()
            .year(19)
            .semester(1)
            .coursesDomain(testCoursePlus1)
            .userDomain(testUser.get()).build();
        completedCoursesDao.save(coursesDomain4);

        GonghakCoursesDomain gonghakCourses3 = GonghakCoursesDomain.builder()
            .courseCategory(CourseCategoryConst.MAJOR_REQUIRED)
            .majorsDomain(TEST_MAJORSDOMAIN)
            .designCredit(1.0)
            .coursesDomain(testCoursePlus1)
            .passCategory("인필")
            .year(19).build();
        gonghakCorusesDao.save(gonghakCourses3);



        //when
        Optional<GonghakResultDto> resultRatio = gonghakCalculateService.getResultRatio(
            testUser.get());

        //then
        Arrays.stream(AbeekTypeConst.values()).forEach(
            abeekType -> {
                assertThat(
                    resultRatio.get().getUserResultRatio().get(abeekType)>=0.0
                ).isEqualTo(true);
            }
        );

        //then
        Map<AbeekTypeConst, Integer> standard = gonghakRepository.findStandard(
            TEST_USERDOMAIN.getStudentId(), TEST_MAJORSDOMAIN).get().getStandards();
        List<AbeekDomain> abeekDomainList = abeekDao.findAllByYearAndMajorsDomain(19,
            TEST_MAJORSDOMAIN);
        abeekDomainList.forEach(
            abeekDomain -> {
                switch (abeekDomain.getAbeekType()){
                    case MAJOR :
                        assertThat(resultRatio.get().getUserResultRatio().get(AbeekTypeConst.MAJOR))
                            .isEqualTo(getExpectedCredit(8,standard,AbeekTypeConst.MAJOR));
                    case DESIGN:
                        assertThat(resultRatio.get().getUserResultRatio().get(AbeekTypeConst.DESIGN))
                            .isEqualTo(getExpectedCredit(2,standard,AbeekTypeConst.DESIGN));
                    case MSC:
                        assertThat(resultRatio.get().getUserResultRatio().get(AbeekTypeConst.MSC))
                            .isEqualTo(getExpectedCredit(7,standard,AbeekTypeConst.MSC));
                    case PROFESSIONAL_NON_MAJOR:
                        assertThat(resultRatio.get().getUserResultRatio().get(AbeekTypeConst.PROFESSIONAL_NON_MAJOR))
                            .isEqualTo(getExpectedCredit(0,standard,AbeekTypeConst.PROFESSIONAL_NON_MAJOR));
                    case MINIMUM_CERTI:
                        assertThat(resultRatio.get().getUserResultRatio().get(AbeekTypeConst.MINIMUM_CERTI))
                            .isEqualTo(getExpectedCredit(15,standard,AbeekTypeConst.MINIMUM_CERTI));
                }
            }
        );
    }

    private double getExpectedCredit(int credit,Map<AbeekTypeConst, Integer> standard,AbeekTypeConst abeekTypeConst) {
        return Double.valueOf(String.format("%.4f",(double) credit / standard.get(abeekTypeConst)));
    }
}