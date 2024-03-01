package com.example.gimmegonghakauth.dao;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.domain.AbeekDomain;
import com.example.gimmegonghakauth.domain.AbeekDomain.AbeekDomainBuilder;
import com.example.gimmegonghakauth.domain.CompletedCoursesDomain;
import com.example.gimmegonghakauth.domain.CoursesDomain;
import com.example.gimmegonghakauth.domain.CoursesDomain.CoursesDomainBuilder;
import com.example.gimmegonghakauth.domain.GonghakCoursesDomain;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.domain.UserDomain.UserDomainBuilder;
import com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto;
import com.example.gimmegonghakauth.dto.GonghakStandardDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//출처: https://0soo.tistory.com/194?category=576925 [Lifealong:티스토리]
class GonghakRepositoryTest {

    private static final Long TEST_STUDENT_ID = 19000001L;

    @Autowired
    private GonghakRepository gonghakRepository;

    @Autowired
    private MajorsDao majorsDao;
    @Autowired
    private AbeekDao abeekDao;
    @Autowired
    private CompletedCoursesDao completedCoursesDao;
    @Autowired
    private CoursesDao coursesDao;
    @Autowired
    private GonghakCorusesDao gonghakCorusesDao;
    @Autowired
    private UserDao userDao;

    private final MajorsDomain testMajorsDomain = MajorsDomain.builder()
        .id(1L)
        .major("건설환경공학과").build();

    @BeforeAll
    void setTable() {
        log.info("----set table----");
        majorsDao.save(testMajorsDomain);

        //AbeekDomain
        AbeekDomainBuilder abeek1 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MAJOR)
            .majorsDomain(testMajorsDomain)
            .note("this is a test note")
            .year(19)
            .minCredit(54);
        AbeekDomainBuilder abeek2 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MSC)
            .majorsDomain(testMajorsDomain)
            .note("this is a test note")
            .year(19)
            .minCredit(30);
        AbeekDomainBuilder abeek3 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.DESIGN)
            .majorsDomain(testMajorsDomain)
            .note("this is a test note")
            .year(19)
            .minCredit(12);
        AbeekDomainBuilder abeek4 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.PROFESSIONAL_NON_MAJOR)
            .majorsDomain(testMajorsDomain)
            .note("this is a test note")
            .year(19)
            .minCredit(14);
        AbeekDomainBuilder abeek5 = AbeekDomain.builder()
            .abeekType(AbeekTypeConst.MINIMUM_CERTI)
            .majorsDomain(testMajorsDomain)
            .note("this is a test note")
            .year(19)
            .minCredit(103);

        abeekDao.save(abeek1.build());
        abeekDao.save(abeek2.build());
        abeekDao.save(abeek3.build());
        abeekDao.save(abeek4.build());
        abeekDao.save(abeek5.build());

        //User
        UserDomain userDomain = UserDomain.builder()
            .email("testEmail")
            .name("홍지섭")
            .password("qwer")
            .studentId(TEST_STUDENT_ID)
            .majorsDomain(testMajorsDomain).build();
        userDao.save(userDomain);

        //Courses
        CoursesDomain testCourse1 = CoursesDomain.builder()
            .courseId(1234L)
            .credit(3)
            .name("testCourse1").build();
        CoursesDomain testCourse2 = CoursesDomain.builder()
            .courseId(2345L)
            .credit(4)
            .name("testCourse2").build();
        CoursesDomain testCourse3 = CoursesDomain.builder()
            .courseId(9000L)
            .credit(5)
            .name("testCourse3").build();
        coursesDao.save(testCourse1);
        coursesDao.save(testCourse2);
        coursesDao.save(testCourse3);

        //CompletedCourses
        CompletedCoursesDomain coursesDomain1 = CompletedCoursesDomain.builder()
            .year(19)
            .semester(1)
            .coursesDomain(testCourse1)
            .userDomain(userDomain).build();
        CompletedCoursesDomain coursesDomain2 = CompletedCoursesDomain.builder()
            .year(19)
            .semester(1)
            .coursesDomain(testCourse2)
            .userDomain(userDomain).build();
        CompletedCoursesDomain coursesDomain3 = CompletedCoursesDomain.builder()
            .year(20)
            .semester(1)
            .coursesDomain(testCourse3)
            .userDomain(userDomain).build();

        completedCoursesDao.save(coursesDomain1);
        completedCoursesDao.save(coursesDomain2);
        completedCoursesDao.save(coursesDomain3);

        //gonghakCourses
        GonghakCoursesDomain gonghakCourses1 = GonghakCoursesDomain.builder()
            .courseCategory("전필")
            .majorsDomain(testMajorsDomain)
            .designCredit(1.5)
            .coursesDomain(testCourse1)
            .passCategory("인필")
            .year(19).build();

        GonghakCoursesDomain gonghakCourses2 = GonghakCoursesDomain.builder()
            .courseCategory("MSC")
            .majorsDomain(testMajorsDomain)
            .designCredit(0.5)
            .coursesDomain(testCourse2)
            .passCategory("인필")
            .year(19).build();

        GonghakCoursesDomain gonghakCourses3 = GonghakCoursesDomain.builder()
            .courseCategory("전선")
            .majorsDomain(testMajorsDomain)
            .designCredit(1.0)
            .coursesDomain(testCourse1)
            .passCategory("인선")
            .year(19).build();

        gonghakCorusesDao.save(gonghakCourses1);
        gonghakCorusesDao.save(gonghakCourses2);
        gonghakCorusesDao.save(gonghakCourses3);
    }

    //GonghakStandardDto 상태 확인
    @Test
    @Rollback
    @DisplayName("GonghakStandardDto 5가지 상태 모두 포함되어있는지 확인")
    void findStandardKeySetTest() {
        Optional<GonghakStandardDto> standard = gonghakRepository.findStandard(TEST_STUDENT_ID, testMajorsDomain);
        log.info("testStandard status ={}", standard.get().getStandards());
        Map<AbeekTypeConst, Integer> testStandard = standard.get().getStandards();
        assertThat(testStandard.keySet()).contains(AbeekTypeConst.values());
    }

    @Test
    @DisplayName("findUserCoursesByMajorByGonghakCoursesWithCompletedCourses 테스트 ")
    void findUserCoursesByMajorByGonghakCoursesWithCompletedCoursesTest() {
        List<GonghakCoursesByMajorDto> userDataForCalculate = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithCompletedCourses(
            TEST_STUDENT_ID, testMajorsDomain);

        List<String> passCategories = new ArrayList<>();
        List<String> courseCategories = new ArrayList<>();
        userDataForCalculate.forEach(gonghakCoursesByMajorDto -> {
            passCategories.add(gonghakCoursesByMajorDto.getPassCategory());
            courseCategories.add(gonghakCoursesByMajorDto.getCourseCategory());
        });

        assertThat(passCategories).containsAll(List.of("인필","인선"));

        assertThat(courseCategories).containsAnyElementsOf(List.of("MSC","전필","전선","전문교양"));
        assertThat(courseCategories).contains("MSC","전필","전선");
    }
}