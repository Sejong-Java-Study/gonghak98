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
        Optional<GonghakStandardDto> standard = gonghakRepository.findStandard(
            userDomain.getStudentId(), userDomain.getMajorsDomain());

        //default user abeek 학점 상태 map
        Map<AbeekTypeConst, Double> userAbeekCredit = new ConcurrentHashMap<>();
        Arrays.stream(AbeekTypeConst.values()).forEach(abeekTypeConst -> {
            userAbeekCredit.put(abeekTypeConst,0.0);
        });

        //user 공학 상태 테이블
        List<GonghakCoursesByMajorDto> userCoursesByMajorByGonghakCoursesWithCompletedCourses = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithCompletedCourses(
            userDomain.getStudentId(), userDomain.getMajorsDomain());

        //user
        userCoursesByMajorByGonghakCoursesWithCompletedCourses.forEach(gonghakCoursesByMajorDto -> {
            switch (gonghakCoursesByMajorDto.getCourseCategory()){
                case "전선":
                    userAbeekCredit.put(AbeekTypeConst.MAJOR,
                        (double) gonghakCoursesByMajorDto.getCredit());
                    userAbeekCredit.put(AbeekTypeConst.DESIGN,
                        gonghakCoursesByMajorDto.getDesignCredit());
                case "전필":
                    userAbeekCredit.put(AbeekTypeConst.MAJOR,
                        (double) gonghakCoursesByMajorDto.getCredit());
                    userAbeekCredit.put(AbeekTypeConst.DESIGN,
                        gonghakCoursesByMajorDto.getDesignCredit());
                case "전문교양":
                    userAbeekCredit.put(AbeekTypeConst.PROFESSIONAL_NON_MAJOR,
                        (double) gonghakCoursesByMajorDto.getCredit());
                case "MSC":
                    userAbeekCredit.put(AbeekTypeConst.MSC,
                        (double) gonghakCoursesByMajorDto.getCredit());
            }
            userAbeekCredit.put(AbeekTypeConst.MINIMUM_CERTI,
                (double) gonghakCoursesByMajorDto.getCredit());

        });

        Optional<GonghakStandardDto> gonghakStandard = gonghakRepository.findStandard(
            userDomain.getStudentId(), userDomain.getMajorsDomain());

        Map<AbeekTypeConst, Double> userResultRatio = new ConcurrentHashMap<>();
        Arrays.stream(AbeekTypeConst.values()).forEach(abeekTypeConst -> {
            userResultRatio.put(
                    abeekTypeConst, (double) gonghakStandard.get().getStandards().get(abeekTypeConst)/standard.get().getStandards().get(abeekTypeConst)
                );
            }
        );

        return Optional.of(new GonghakResultDto(userResultRatio));
    }


}
