package com.example.gimmegonghakauth.dao;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.constant.CourseCategoryConst;
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
import com.example.gimmegonghakauth.dto.IncompletedCoursesDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Data;
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

    private final MajorsDomain testMajorsDomain = MajorsDomain.builder()
        .id(1L)
        .major("컴퓨터공학과").build();

    //출력
    @Test
    @DisplayName("dao 메서드 상태 출력")
    void displayDaoMethod(){
        List<IncompletedCoursesDto> withoutCompleteCourses = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
            CourseCategoryConst.MAJOR_SELECTIVE, 19011706L, testMajorsDomain
        );

        withoutCompleteCourses.forEach(
            incompletedCoursesDto -> {
                log.info("withoutCompleteCourses = {}:", incompletedCoursesDto.getCourseName());
            }
        );

        List<GonghakCoursesByMajorDto> withCompletedCourses = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithCompletedCourses(
            TEST_STUDENT_ID, testMajorsDomain
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
        List<CourseCategoryConst> courseCategories = new ArrayList<>();
        userDataForCalculate.forEach(gonghakCoursesByMajorDto -> {
            passCategories.add(gonghakCoursesByMajorDto.getPassCategory());
            courseCategories.add(gonghakCoursesByMajorDto.getCourseCategory());
        });

        assertThat(passCategories).containsAll(List.of("인필","인선"));

        assertThat(courseCategories).containsAnyElementsOf(List.of(CourseCategoryConst.MAJOR_REQUIRED,CourseCategoryConst.MAJOR_SELECTIVE,CourseCategoryConst.MSC,CourseCategoryConst.PROFESSIONAL_NON_MAJOR));
        assertThat(courseCategories).contains(CourseCategoryConst.MSC,CourseCategoryConst.MAJOR_SELECTIVE);
    }

    @Test
    @DisplayName("findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses")
    void findUserCoursesByMajorByGonghakCoursesWithoutCompleteCoursesTest(){

        Arrays.stream(CourseCategoryConst.values()).forEach(
            courseCategory -> {
                List<IncompletedCoursesDto> testCourses = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
                    CourseCategoryConst.MAJOR_REQUIRED,
                    19011706L,
                    testMajorsDomain
                );

                testCourses.forEach(
                    incompletedCoursesDto -> {
                        assertThat(incompletedCoursesDto.getCourseCategory()).isEqualTo(CourseCategoryConst.MAJOR_REQUIRED);
                    }
                );
            }
        );
    }
}