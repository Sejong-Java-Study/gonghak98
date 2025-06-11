package com.example.gimmegonghakauth.common.service;

import com.example.gimmegonghakauth.common.constant.CourseCategoryConst;
import com.example.gimmegonghakauth.common.domain.CoursesDomain;
import com.example.gimmegonghakauth.common.infrastructure.CoursesDao;
import com.example.gimmegonghakauth.common.infrastructure.MajorsDao;
import com.example.gimmegonghakauth.status.domain.GonghakCoursesDomain;
import com.example.gimmegonghakauth.status.infrastructure.GonghakCoursesDao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Profile("!(prod || release)")
@Slf4j
public class InitFileData {

    private final MajorsDao majorsDao;
    private final CoursesDao coursesDao;
    private final GonghakCoursesDao gonghakCoursesDao;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void loadCoursesDataFromCSV() throws IOException {
        String courseCsvClasspath = "csv/course.csv";
        String gonghakCourseCsvClasspath = "csv/gonghak_course.csv";

        try (InputStream courseInputStream = new ClassPathResource(
            courseCsvClasspath).getInputStream()) {
            inputCoursesCsv(courseInputStream);
        } catch (IOException e) {
            log.error("course.csv 로드 실패 : {}", e.getMessage());
            throw new IllegalArgumentException("클래스 패스로부터 csv 파일 로딩 실패", e);
        }

        try (InputStream gonghakCourseInputStream = new ClassPathResource(
            gonghakCourseCsvClasspath).getInputStream()) {
            inputGonghakCoursesCsv(gonghakCourseInputStream);
        } catch (IOException e) {
            log.error("gonghak_course.csv 로드 실패 : {}", e.getMessage());
            throw new IllegalArgumentException("클래스 패스로부터 csv 파일 로딩 실패", e);
        }
    }

    private void inputCoursesCsv(InputStream inputStream) throws IOException {
        String line;
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                try {
                    CoursesDomain course = mapToCoursesDomain(data);
                    coursesDao.save(course);
                } catch (Exception e) {
                    log.error("error contents : {}", line);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private CoursesDomain mapToCoursesDomain(String[] data) {
        return CoursesDomain.builder()
            .courseId(Long.valueOf(data[0]))
            .name(data[2])
            .credit(Integer.parseInt(data[3]))
            .build();
    }

    private void inputGonghakCoursesCsv(InputStream inputStream) {
        String line;
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            br.readLine(); // 첫 번째 헤더 라인 건너뛰기
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                try {
                    Optional<GonghakCoursesDomain> course = mapToGonghakCourses(data);
                    course.ifPresent(gonghakCoursesDao::save);
                } catch (Exception e) {
                    log.error("error contents : {}", line);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // raw file 입력 용
    private Optional<GonghakCoursesDomain> mapToGonghakCoursesDomain(String[] data) {

        CoursesDomain courseDomain = coursesDao.findByNameIgnoreSpaces(
            data[6].replaceAll("\\s+", ""));
        if (courseDomain == null) {
            return Optional.empty();
        }

        // course_category_const
        String courseCategory = data[4];

        switch (courseCategory) {
            case "중핵필수", "교양필수", "교양선택", "교양선택I", "교양":
                courseCategory = "전문교양";
                break;
            case "전공기초교양", "학문기초교양":
                courseCategory = "BSM";
                break;
            case "전공필수", "전공선택", "전공(설계)", "전공주제", "전공기초":
                courseCategory = "전공";
                break;
            default:
                break;
        }

        GonghakCoursesDomain gonghakCourse = GonghakCoursesDomain.builder()
            .year(Integer.parseInt(data[0]))
            .majorsDomain(majorsDao.findByMajor(data[2]))
            .coursesDomain(courseDomain)
            .courseCategory(CourseCategoryConst.valueOf(courseCategory))
            .passCategory(data[5].substring(0, 2))
            .designCredit(Double.parseDouble(data[8]))
            .build();

        return Optional.of(gonghakCourse);
    }

    // 실제 DB csv file 입력용
    private Optional<GonghakCoursesDomain> mapToGonghakCourses(String[] data) {
        GonghakCoursesDomain gonghakCourse = GonghakCoursesDomain.builder()
            .year(Integer.parseInt(data[1]))
            .majorsDomain(majorsDao.findById(Long.parseLong(data[4])).get())
            .coursesDomain(coursesDao.findByCourseId(Long.parseLong(data[2])))
            .courseCategory(CourseCategoryConst.valueOf(data[5]))
            .passCategory(data[6])
            .designCredit(Double.parseDouble(data[0]))
            .build();

        return Optional.of(gonghakCourse);
    }
}
