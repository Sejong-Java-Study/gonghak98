package com.example.gimmegonghakauth.service;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.dao.GonghakRepository;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.dto.GonghakCoursesByMajorDto;
import com.example.gimmegonghakauth.dto.GonghakResultDto;
import com.example.gimmegonghakauth.dto.GonghakStandardDto;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GonghakCalculateService {

    private final GonghakRepository gonghakRepository;

    public Optional<GonghakResultDto> getResultRatio(UserDomain userDomain) {
        //standard
        Optional<GonghakStandardDto> standard = gonghakRepository.findStandard(userDomain.getStudentId(), userDomain.getMajorsDomain());

        //default user abeek 학점 상태 map
        Map<AbeekTypeConst, Double> userAbeekCredit = getUserAbeekCreditDefault();

        //user 공학 상태 테이블
        List<GonghakCoursesByMajorDto> userCoursesByMajorByGonghakCoursesWithCompletedCourses = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithCompletedCourses(
            userDomain.getStudentId(), userDomain.getMajorsDomain());

        //user
        stackUserGonghakCredit(userCoursesByMajorByGonghakCoursesWithCompletedCourses, userAbeekCredit);

        Map<AbeekTypeConst, Double> userResultRatio = getUserGonghakResultRatio(userDomain, standard);

        return Optional.of(new GonghakResultDto(userResultRatio));
    }

    private static Map<AbeekTypeConst, Double> getUserAbeekCreditDefault() {
        Map<AbeekTypeConst, Double> userAbeekCredit = new ConcurrentHashMap<>();
        Arrays.stream(AbeekTypeConst.values()).forEach(abeekTypeConst -> {
            userAbeekCredit.put(abeekTypeConst,0.0);
        });
        return userAbeekCredit;
    }

    private Map<AbeekTypeConst, Double> getUserGonghakResultRatio(UserDomain userDomain,
        Optional<GonghakStandardDto> standard) {
        Optional<GonghakStandardDto> gonghakStandard = gonghakRepository.findStandard(
            userDomain.getStudentId(), userDomain.getMajorsDomain());

        Map<AbeekTypeConst, Double> userResultRatio = new ConcurrentHashMap<>();
        Arrays.stream(AbeekTypeConst.values()).forEach(abeekTypeConst -> {
            userResultRatio.put(
                    abeekTypeConst, (double) gonghakStandard.get().getStandards().get(abeekTypeConst)/ standard.get().getStandards().get(abeekTypeConst)
                );
            }
        );
        return userResultRatio;
    }

    private void stackUserGonghakCredit(
        List<GonghakCoursesByMajorDto> userCoursesByMajorByGonghakCoursesWithCompletedCourses,
        Map<AbeekTypeConst, Double> userAbeekCredit) {
        userCoursesByMajorByGonghakCoursesWithCompletedCourses.forEach(gonghakCoursesByMajorDto -> {
            switch (gonghakCoursesByMajorDto.getCourseCategory()){
                case "전선":
                    stackCredit(AbeekTypeConst.MAJOR, gonghakCoursesByMajorDto, userAbeekCredit);
                    stackCredit(AbeekTypeConst.DESIGN, gonghakCoursesByMajorDto, userAbeekCredit);
                case "전필":
                    stackCredit(AbeekTypeConst.MAJOR, gonghakCoursesByMajorDto, userAbeekCredit);
                    userAbeekCredit.put(AbeekTypeConst.DESIGN, gonghakCoursesByMajorDto.getDesignCredit());
                case "전문교양":
                    stackCredit(AbeekTypeConst.PROFESSIONAL_NON_MAJOR,gonghakCoursesByMajorDto,
                        userAbeekCredit);
                case "MSC":
                    stackCredit(AbeekTypeConst.MSC,gonghakCoursesByMajorDto, userAbeekCredit);
            }
            stackCredit(AbeekTypeConst.MINIMUM_CERTI,gonghakCoursesByMajorDto, userAbeekCredit);

        });
    }

    private void stackCredit(AbeekTypeConst abeekTypeConst, GonghakCoursesByMajorDto gonghakCoursesByMajorDto,
        Map<AbeekTypeConst, Double> userAbeekCredit) {
        userAbeekCredit.put(abeekTypeConst,
            userAbeekCredit.get(abeekTypeConst) + (double) gonghakCoursesByMajorDto.getCredit());
    }


}
