package com.example.gimmegonghakauth.service;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.dao.GonghakRepository;
import com.example.gimmegonghakauth.domain.AbeekDomain;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto;
import com.example.gimmegonghakauth.dto.GonghakResultDto;
import com.example.gimmegonghakauth.dto.GonghakStandardDto;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.gimmegonghakauth.constant.CourseCategoryConst;

@Service
@RequiredArgsConstructor
@Slf4j
public class GonghakCalculateService {

    private final GonghakRepository gonghakRepository;

    private static final DecimalFormat RESULT_RATIO_FORMAT = new DecimalFormat("#.####");

    public Optional<GonghakResultDto> getResultRatio(UserDomain userDomain) {
        //standard
        Optional<GonghakStandardDto> standard = gonghakRepository.findStandard(userDomain.getStudentId(), userDomain.getMajorsDomain());

        //default user abeek 학점 상태 map
        Map<AbeekTypeConst, Double> userAbeekCredit = getUserAbeekCreditDefault();

        log.info("default user abeek 학점 상태 map userAbeekCredit = {}",userAbeekCredit);

        //user 공학 상태 테이블
        List<GonghakCoursesByMajorDto> userCoursesByMajorByGonghakCoursesWithCompletedCourses = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithCompletedCourses(
            userDomain.getStudentId(), userDomain.getMajorsDomain());

        log.info("user 공학 상태 테이블 userCoursesByMajorByGonghakCoursesWithCompletedCourses ={}",userCoursesByMajorByGonghakCoursesWithCompletedCourses);

        //user
        stackUserGonghakCredit(userCoursesByMajorByGonghakCoursesWithCompletedCourses, userAbeekCredit);

        log.info("학점 계산 후 학점 상태 map userAbeekCredit = {}",userAbeekCredit);

        Map<AbeekTypeConst, Double> userResultRatio = getUserGonghakResultRatio(userAbeekCredit, standard);

        log.info("비율 결과 userResultRatio = {}",userResultRatio);

        return Optional.of(new GonghakResultDto(userResultRatio));
    }

    private Map<AbeekTypeConst, Double> getUserAbeekCreditDefault() {
        Map<AbeekTypeConst, Double> userAbeekCredit = new ConcurrentHashMap<>();
        Arrays.stream(AbeekTypeConst.values()).forEach(abeekTypeConst -> {
            userAbeekCredit.put(abeekTypeConst,0.0);
        });
        return userAbeekCredit;
    }

    private Map<AbeekTypeConst, Double> getUserGonghakResultRatio(Map<AbeekTypeConst, Double> userAbeekCredit,
        Optional<GonghakStandardDto> standard) {

        Map<AbeekTypeConst, Double> userResultRatio = new ConcurrentHashMap<>();
        Arrays.stream(AbeekTypeConst.values()).forEach(abeekTypeConst -> {
            getRatio(userAbeekCredit, standard, abeekTypeConst, userResultRatio);
            }
        );
        return userResultRatio;
    }

    private Double getRatio(Map<AbeekTypeConst, Double> userAbeekCredit,
        Optional<GonghakStandardDto> standard, AbeekTypeConst abeekTypeConst,
        Map<AbeekTypeConst, Double> userResultRatio) {
        return userResultRatio.put(
            abeekTypeConst, Double.valueOf(
                RESULT_RATIO_FORMAT.format(
                    (double) userAbeekCredit.get(abeekTypeConst) / standard.get().getStandards()
                        .get(abeekTypeConst)))
        );
    }

    private void stackUserGonghakCredit(
        List<GonghakCoursesByMajorDto> userCoursesByMajorByGonghakCoursesWithCompletedCourses,
        Map<AbeekTypeConst, Double> userAbeekCredit) {
        userCoursesByMajorByGonghakCoursesWithCompletedCourses.forEach(gonghakCoursesByMajorDto -> {
            switch (gonghakCoursesByMajorDto.getCourseCategory()){
                case MAJOR_SELECTIVE:
                    stackCredit(AbeekTypeConst.MAJOR, gonghakCoursesByMajorDto, userAbeekCredit);break;
                case MAJOR_REQUIRED:
                    stackCredit(AbeekTypeConst.MAJOR, gonghakCoursesByMajorDto, userAbeekCredit);break;
                case PROFESSIONAL_NON_MAJOR:
                    stackCredit(AbeekTypeConst.PROFESSIONAL_NON_MAJOR,gonghakCoursesByMajorDto, userAbeekCredit); break;
                case MSC:
                    stackCredit(AbeekTypeConst.MSC,gonghakCoursesByMajorDto, userAbeekCredit); break;
            }
            stackCredit(AbeekTypeConst.DESIGN, gonghakCoursesByMajorDto, userAbeekCredit);
            stackCredit(AbeekTypeConst.MINIMUM_CERTI,gonghakCoursesByMajorDto, userAbeekCredit);
        });
    }

    private void stackCredit(AbeekTypeConst abeekTypeConst, GonghakCoursesByMajorDto gonghakCoursesByMajorDto,
        Map<AbeekTypeConst, Double> userAbeekCredit) {
        double inputCredit = getInputCredit(abeekTypeConst, gonghakCoursesByMajorDto);

        userAbeekCredit.put(abeekTypeConst,
            userAbeekCredit.get(abeekTypeConst) + inputCredit);

    }

    private double getInputCredit(AbeekTypeConst abeekTypeConst,
        GonghakCoursesByMajorDto gonghakCoursesByMajorDto) {
        if(abeekTypeConst == AbeekTypeConst.DESIGN) return gonghakCoursesByMajorDto.getDesignCredit();
        else return (double) gonghakCoursesByMajorDto.getCredit();
    }


}
