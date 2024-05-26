package com.example.gimmegonghakauth.dao;

import static com.example.gimmegonghakauth.constant.CourseCategoryConst.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.constant.CourseCategoryConst;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto;
import com.example.gimmegonghakauth.dto.GonghakStandardDto;
import com.example.gimmegonghakauth.dto.IncompletedCoursesDto;
import com.example.gimmegonghakauth.service.recommend.MajorName;
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

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GonghakRepositoryTest {
<<<<<<< HEAD

    private static final Long COM_TEST_STUDENT_ID = 19011706L;
=======
    private static final Long TEST_STUDENT_ID = 19011706L;
>>>>>>> heesu-test

    @Autowired
    private GonghakRepository gonghakRepository;
    @Autowired
    private MajorsDao majorsDao;

    private MajorsDomain COM_TEST_MAJORDOMAIN;

    private MajorsDomain WRONG_TEST_MAJORDOMAIN;

    @BeforeAll
    void setInit(){
        COM_TEST_MAJORDOMAIN = majorsDao.findByMajor(MajorName.COMPUTER.getName());
        WRONG_TEST_MAJORDOMAIN = MajorsDomain.builder()
            .id(3L)
            .major("오징어먹물학과").build();
    }

    //출력
    @Test
    @DisplayName("dao 메서드 상태 출력")
    void displayDaoMethod(){
        List<IncompletedCoursesDto> withoutCompleteCourses = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
<<<<<<< HEAD
            CourseCategoryConst.전선, COM_TEST_STUDENT_ID, COM_TEST_MAJORDOMAIN
=======
<<<<<<< HEAD
            MAJOR_SELECTIVE, STUDENT_ID, TEST_MAJORDOMAIN
=======
            CourseCategoryConst.전선, STUDENT_ID, TEST_MAJORDOMAIN
>>>>>>> SJ-91-페이지-화면-구현
>>>>>>> heesu-test
        );

        withoutCompleteCourses.forEach(
            incompletedCoursesDto -> {
                log.info("withoutCompleteCourses = {}:", incompletedCoursesDto.getCourseName());
            }
        );

        List<GonghakCoursesByMajorDto> withCompletedCourses = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithCompletedCourses(
            COM_TEST_STUDENT_ID, COM_TEST_MAJORDOMAIN
        );

        withCompletedCourses.forEach(
            gonghakCoursesByMajorDto -> {
                log.info("withCompletedCourses = {}",gonghakCoursesByMajorDto.getCourseName());
            }
        );
    }
    //GonghakStandardDto 상태 확인
    @Test
    @DisplayName("GonghakStandardDto 5가지 상태 모두 포함되어있는지 확인")
    void findStandardKeySetTest() {
        Optional<GonghakStandardDto> standard = gonghakRepository.findStandard(COM_TEST_STUDENT_ID, COM_TEST_MAJORDOMAIN);
        log.info("testStandard status ={}", standard.get().getStandards());
        Map<AbeekTypeConst, Integer> testStandard = standard.get().getStandards();
        assertThat(testStandard.keySet()).contains(AbeekTypeConst.BSM,AbeekTypeConst.PROFESSIONAL_NON_MAJOR,AbeekTypeConst.DESIGN,AbeekTypeConst.MAJOR,AbeekTypeConst.MINIMUM_CERTI);
        assertThat(testStandard.containsKey(AbeekTypeConst.MSC)).isEqualTo(false);
    }

    @Test
    @DisplayName("findUserCoursesByMajorByGonghakCoursesWithCompletedCourses 테스트 ")
    void findUserCoursesByMajorByGonghakCoursesWithCompletedCoursesTest() {
        List<GonghakCoursesByMajorDto> userDataForCalculate = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithCompletedCourses(
            COM_TEST_STUDENT_ID, COM_TEST_MAJORDOMAIN);

        log.info("userDataForCalculate size = {}",userDataForCalculate.size());
        for (GonghakCoursesByMajorDto course : userDataForCalculate) {
            log.info("Course ID: {}, Course Name: {}, Year: {}, Course Category: {}, Pass Category: {}, Design Credit: {}, Credit: {}",
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

<<<<<<< HEAD
        assertThat(passCategories).containsAll(List.of("인필", "인선"));
        assertThat(courseCategories).containsAnyElementsOf(List.of(MAJOR_REQUIRED, MAJOR_SELECTIVE, MSC,
            PROFESSIONAL_NON_MAJOR));
        assertThat(courseCategories).contains(MSC, MAJOR_SELECTIVE);
=======
        assertThat(passCategories).containsAll(List.of("인필","인선"));

<<<<<<< HEAD
        assertThat(courseCategories).containsAnyElementsOf(List.of(CourseCategoryConst.전문교양,CourseCategoryConst.전공주제,CourseCategoryConst.BSM));
=======
        assertThat(courseCategories).containsAnyElementsOf(List.of(CourseCategoryConst.전필,CourseCategoryConst.전선,CourseCategoryConst.MSC,CourseCategoryConst.전문교양));
        assertThat(courseCategories).contains(CourseCategoryConst.MSC,CourseCategoryConst.전선);
>>>>>>> SJ-91-페이지-화면-구현
>>>>>>> heesu-test
    }

    @Test
    @DisplayName("findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses")
    void findUserCoursesByMajorByGonghakCoursesWithoutCompleteCoursesTest(){
        Arrays.stream(values()).forEach(
            courseCategory -> {
                List<IncompletedCoursesDto> testCourses = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
<<<<<<< HEAD
                    MAJOR_REQUIRED,
=======
                    CourseCategoryConst.전필,
<<<<<<< HEAD
                    COM_TEST_STUDENT_ID,
                    COM_TEST_MAJORDOMAIN
=======
>>>>>>> SJ-91-페이지-화면-구현
                    STUDENT_ID,
                    TEST_MAJORDOMAIN
>>>>>>> heesu-test
                );

                testCourses.forEach(
                    incompletedCoursesDto -> {
<<<<<<< HEAD
                        assertThat(incompletedCoursesDto.getCourseCategory()).isEqualTo(MAJOR_REQUIRED);
=======
                        assertThat(incompletedCoursesDto.getCourseCategory()).isEqualTo(CourseCategoryConst.전필);
>>>>>>> SJ-91-페이지-화면-구현
                    }
                );
            }
        );
    }

    @Test
    @DisplayName("findStandard가 없을 때 - Wrong Major")
    void findStandardWrongMajorDomainTest(){
        Optional<GonghakStandardDto> wrongStandard = gonghakRepository.findStandard(
            COM_TEST_STUDENT_ID,
            WRONG_TEST_MAJORDOMAIN);
        assertThat(wrongStandard.get().getStandards().isEmpty()).isEqualTo(true);
    }
}
