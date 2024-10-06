package com.example.gimmegonghakauth.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.constant.CourseCategoryConst;
import com.example.gimmegonghakauth.domain.CompletedCoursesDomain;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.user.domain.UserDomain;
import com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto;
import com.example.gimmegonghakauth.dto.GonghakStandardDto;
import com.example.gimmegonghakauth.dto.IncompletedCoursesDto;
import com.example.gimmegonghakauth.service.recommend.MajorName;
import com.example.gimmegonghakauth.user.infrastructure.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.test.context.ActiveProfiles;


@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class GonghakRepositoryTest {

    private static final Long COM_TEST_STUDENT_ID = 19111111L;

    @Autowired
    private GonghakRepository gonghakRepository;
    @Autowired
    private MajorsDao majorsDao;
    @Autowired
    private CompletedCoursesDao completedCoursesDao;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CoursesDao coursesDao;

    private MajorsDomain COM_TEST_MAJORDOMAIN;

    private MajorsDomain WRONG_TEST_MAJORDOMAIN;


    @BeforeAll
    void setInit() {
        setMajor();
        setUserAndCompletedCourse();
    }

    void setMajor() {
        COM_TEST_MAJORDOMAIN = majorsDao.findByMajor(MajorName.COMPUTER.getName());
        WRONG_TEST_MAJORDOMAIN = MajorsDomain.builder()
            .id(5L)
            .major("오징어먹물학과").build();
    }

    void setUserAndCompletedCourse() {
        UserDomain user = UserDomain.builder().studentId(19111111L)
            .password("qwer").email("test@sju.com")
            .majorsDomain(majorsDao.findByMajor("컴퓨터공학과"))
            .name("testUser")
            .build();
        userRepository.save(user);

        CompletedCoursesDomain course1 = CompletedCoursesDomain.builder()
            .coursesDomain(coursesDao.findByName("Capstone디자인(산학협력프로젝트)"))
            .year(19)
            .userDomain(user)
            .semester("1학기")
            .build();

        CompletedCoursesDomain course2 = CompletedCoursesDomain.builder()
            .coursesDomain(coursesDao.findByName("웹프로그래밍"))
            .year(19)
            .userDomain(user)
            .semester("1학기")
            .build();

        completedCoursesDao.save(course1);
        completedCoursesDao.save(course2);
    }


    //출력
    @Test
    @DisplayName("dao 메서드 상태 출력")
    void displayDaoMethod() {
        List<IncompletedCoursesDto> withoutCompleteCourses = gonghakRepository.findUserIncompletedCourses(
            CourseCategoryConst.전공, COM_TEST_STUDENT_ID, COM_TEST_MAJORDOMAIN
        );

        withoutCompleteCourses.forEach(
            incompletedCoursesDto -> {
                log.info("withoutCompleteCourses = {}:", incompletedCoursesDto.getCourseName());
            }
        );

        List<GonghakCoursesByMajorDto> withCompletedCourses = gonghakRepository.findUserCompletedCourses(
            COM_TEST_STUDENT_ID, COM_TEST_MAJORDOMAIN
        );

        withCompletedCourses.forEach(
            gonghakCoursesByMajorDto -> {
                log.info("withCompletedCourses = {}", gonghakCoursesByMajorDto.getCourseName());
            }
        );
    }

    //GonghakStandardDto 상태 확인
    @Test
    @DisplayName("GonghakStandardDto 5가지 상태 모두 포함되어있는지 확인")
    void findStandardKeySetTest() {
        Optional<GonghakStandardDto> standard = gonghakRepository.findStandard(
            COM_TEST_MAJORDOMAIN);
        log.info("testStandard status ={}", standard.get().getStandards());
        Map<AbeekTypeConst, Integer> testStandard = standard.get().getStandards();
        assertThat(testStandard.keySet()).contains(AbeekTypeConst.BSM,
            AbeekTypeConst.PROFESSIONAL_NON_MAJOR, AbeekTypeConst.DESIGN, AbeekTypeConst.MAJOR,
            AbeekTypeConst.MINIMUM_CERTI);
        assertThat(testStandard.containsKey(AbeekTypeConst.MSC)).isEqualTo(false);
    }


    @Test
    @DisplayName("findUserCoursesByMajorByGonghakCoursesWithCompletedCourses 테스트 ")
    void findUserCoursesByMajorByGonghakCoursesWithCompletedCoursesTest() {
        List<GonghakCoursesByMajorDto> userDataForCalculate = gonghakRepository.findUserCompletedCourses(
            COM_TEST_STUDENT_ID, COM_TEST_MAJORDOMAIN);

        log.info("userDataForCalculate size = {}", userDataForCalculate.size());
        for (GonghakCoursesByMajorDto course : userDataForCalculate) {
            log.info(
                "Course ID: {}, Course Name: {}, Year: {}, Course Category: {}, Pass Category: {}, Design Credit: {}, Credit: {}",
                course.getCourseId(),
                course.getCourseName(),
                course.getYear(),
                course.getCourseCategory(),
                course.getPassCategory(),
                course.getDesignCredit(),
                course.getCredit());
        }

        List<String> passCategories = new ArrayList<>();
        List<CourseCategoryConst> courseCategories = new ArrayList<>();
        userDataForCalculate.forEach(gonghakCoursesByMajorDto -> {
            passCategories.add(gonghakCoursesByMajorDto.getPassCategory());
            courseCategories.add(gonghakCoursesByMajorDto.getCourseCategory());
        });

        assertThat(passCategories).containsAll(List.of("인필", "인선"));

        assertThat(courseCategories).containsAnyElementsOf(
            List.of(CourseCategoryConst.전문교양, CourseCategoryConst.전공, CourseCategoryConst.BSM));
    }

    @Test
    @DisplayName("findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses")
    void findUserCoursesByMajorByGonghakCoursesWithoutCompleteCoursesTest() {

        Arrays.stream(CourseCategoryConst.values()).forEach(
            courseCategory -> {
                List<IncompletedCoursesDto> testCourses = gonghakRepository.findUserIncompletedCourses(
                    CourseCategoryConst.전공,
                    COM_TEST_STUDENT_ID,
                    COM_TEST_MAJORDOMAIN
                );

                testCourses.forEach(
                    incompletedCoursesDto -> {
                        assertThat(incompletedCoursesDto.getCourseCategory()).isEqualTo(
                            CourseCategoryConst.전공);
                    }
                );
            }
        );
    }

    @Test
    @DisplayName("findStandard가 없을 때 - Wrong Major")
    void findStandardWrongMajorDomainTest() {
        Optional<GonghakStandardDto> wrongStandard = gonghakRepository.findStandard(
            WRONG_TEST_MAJORDOMAIN);
        assertThat(wrongStandard.get().getStandards().isEmpty()).isEqualTo(true);
    }
}
