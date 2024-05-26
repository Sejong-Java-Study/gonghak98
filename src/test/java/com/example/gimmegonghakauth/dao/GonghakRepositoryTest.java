package com.example.gimmegonghakauth.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.constant.CourseCategoryConst;
import com.example.gimmegonghakauth.domain.MajorsDomain;
import com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto;
import com.example.gimmegonghakauth.dto.GonghakStandardDto;
import com.example.gimmegonghakauth.dto.IncompletedCoursesDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//출처: https://0soo.tistory.com/194?category=576925
class GonghakRepositoryTest {

    private static final Long TEST_STUDENT_ID = 19011706L;

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

    private final MajorsDomain TEST_MAJORDOMAIN = MajorsDomain.builder()
        .id(1L)
        .major("컴퓨터공학과").build();

    private final MajorsDomain WRONG_TEST_MAJORDOMAIN = MajorsDomain.builder()
        .id(2L)
        .major("오징어먹물학과").build();

    private final Long STUDENT_ID = 19011706L;

    //출력
    @Test
    @DisplayName("dao 메서드 상태 출력")
    void displayDaoMethod(){
        List<IncompletedCoursesDto> withoutCompleteCourses = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
            CourseCategoryConst.전선, STUDENT_ID, TEST_MAJORDOMAIN
        );

        withoutCompleteCourses.forEach(
            incompletedCoursesDto -> {
                log.info("withoutCompleteCourses = {}:", incompletedCoursesDto.getCourseName());
            }
        );

        List<GonghakCoursesByMajorDto> withCompletedCourses = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithCompletedCourses(
            TEST_STUDENT_ID, TEST_MAJORDOMAIN
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
        Optional<GonghakStandardDto> standard = gonghakRepository.findStandard(TEST_STUDENT_ID,
            TEST_MAJORDOMAIN);
        log.info("testStandard status ={}", standard.get().getStandards());
        Map<AbeekTypeConst, Integer> testStandard = standard.get().getStandards();
        assertThat(testStandard.keySet()).contains(AbeekTypeConst.values());
    }


    @Test
    @DisplayName("findUserCoursesByMajorByGonghakCoursesWithCompletedCourses 테스트 ")
    void findUserCoursesByMajorByGonghakCoursesWithCompletedCoursesTest() {
        List<GonghakCoursesByMajorDto> userDataForCalculate = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithCompletedCourses(
            TEST_STUDENT_ID, TEST_MAJORDOMAIN);

        List<String> passCategories = new ArrayList<>();
        List<CourseCategoryConst> courseCategories = new ArrayList<>();
        userDataForCalculate.forEach(gonghakCoursesByMajorDto -> {
            passCategories.add(gonghakCoursesByMajorDto.getPassCategory());
            courseCategories.add(gonghakCoursesByMajorDto.getCourseCategory());
        });

        assertThat(passCategories).containsAll(List.of("인필","인선"));

        assertThat(courseCategories).containsAnyElementsOf(List.of(CourseCategoryConst.전필,CourseCategoryConst.전선,CourseCategoryConst.MSC,CourseCategoryConst.전문교양));
        assertThat(courseCategories).contains(CourseCategoryConst.MSC,CourseCategoryConst.전선);
    }

    @Test
    @DisplayName("findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses")
    void findUserCoursesByMajorByGonghakCoursesWithoutCompleteCoursesTest(){

        Arrays.stream(CourseCategoryConst.values()).forEach(
            courseCategory -> {
                List<IncompletedCoursesDto> testCourses = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
                    CourseCategoryConst.전필,
                    STUDENT_ID,
                    TEST_MAJORDOMAIN
                );

                testCourses.forEach(
                    incompletedCoursesDto -> {
                        assertThat(incompletedCoursesDto.getCourseCategory()).isEqualTo(CourseCategoryConst.전필);
                    }
                );
            }
        );
    }

    @Test
    @DisplayName("정상적인 findStandard")
    void findStandardTest(){
        Optional<GonghakStandardDto> standard = gonghakRepository.findStandard(STUDENT_ID,
            TEST_MAJORDOMAIN);

        assertThat(standard.get().getStandards().get(AbeekTypeConst.MAJOR)).isEqualTo(54);
        assertThat(standard.get().getStandards().get(AbeekTypeConst.MINIMUM_CERTI)).isEqualTo(100);
    }

    @Test
    @DisplayName("findStandard가 없을 때 - Wrong Major")
    void findStandardWrongMajorDomainTest(){
        Optional<GonghakStandardDto> wrongStandard = gonghakRepository.findStandard(STUDENT_ID,
            WRONG_TEST_MAJORDOMAIN);
        assertThat(wrongStandard.get().getStandards().isEmpty()).isEqualTo(true);
    }
}