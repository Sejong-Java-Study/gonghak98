package com.example.gimmegonghakauth.service.recommend;


import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.constant.CourseCategoryConst;
import com.example.gimmegonghakauth.dao.GonghakRepository;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.dto.GonghakRecommendCoursesDto;
import com.example.gimmegonghakauth.dto.GonghakStandardDto;
import com.example.gimmegonghakauth.dto.IncompletedCoursesDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComputerMajorGonghakRecommendService implements GonghakRecommendService {
    private final GonghakRepository gonghakRepository;

    //리팩토링 필요
    @Override
    public GonghakRecommendCoursesDto createRecommendCourses(UserDomain userDomain){
        GonghakRecommendCoursesDto gonghakRecommendCoursesDto = new GonghakRecommendCoursesDto();
        Optional<GonghakStandardDto> standard = gonghakRepository.findStandard(
            userDomain.getStudentId(), userDomain.getMajorsDomain());

        List<IncompletedCoursesDto> majorBasic = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
            CourseCategoryConst.전공기초, userDomain.getStudentId(), userDomain.getMajorsDomain()
        );
        printLog(majorBasic);
        List<IncompletedCoursesDto> majorSubject = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
            CourseCategoryConst.전공주제, userDomain.getStudentId(), userDomain.getMajorsDomain()
        );
        printLog(majorSubject);
        List<IncompletedCoursesDto> nonMajor = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
            CourseCategoryConst.전문교양, userDomain.getStudentId(), userDomain.getMajorsDomain()
        );
        printLog(nonMajor);
        List<IncompletedCoursesDto> bsm = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
            CourseCategoryConst.BSM, userDomain.getStudentId(), userDomain.getMajorsDomain()
        );
        printLog(bsm);

        Map<AbeekTypeConst, List<IncompletedCoursesDto>> coursesByAbeekTypeWithoutCompleteCourses = gonghakRecommendCoursesDto.getRecommendCoursesByAbeekType();
        Arrays.stream(AbeekTypeConst.values()).forEach(
            abeekType -> {
                List<IncompletedCoursesDto> abeekRecommend = new ArrayList<>();
                if(standard.get().getStandards().containsKey(abeekType)){
                    switch (abeekType){
                        case BSM:
                            abeekRecommend.addAll(bsm); break;
                        case MAJOR:
                            abeekRecommend.addAll(majorBasic);
                            abeekRecommend.addAll(majorSubject); break;
                        case DESIGN:
                            addOnlyDesignCreditOverZero(majorBasic, abeekRecommend);
                            addOnlyDesignCreditOverZero(majorSubject, abeekRecommend);
                            break;
                        case PROFESSIONAL_NON_MAJOR:
                            abeekRecommend.addAll(nonMajor); break;
                        case MINIMUM_CERTI:
                            abeekRecommend.addAll(bsm);
                            abeekRecommend.addAll(majorBasic);
                            abeekRecommend.addAll(majorSubject);
                            abeekRecommend.addAll(nonMajor); break;
                    }
                    coursesByAbeekTypeWithoutCompleteCourses.put(abeekType,abeekRecommend);
                }


            }
        );

        return gonghakRecommendCoursesDto;
    }

    private static void addOnlyDesignCreditOverZero(List<IncompletedCoursesDto> majorBasic,
        List<IncompletedCoursesDto> abeekRecommend) {
        majorBasic.forEach(
            incompletedCoursesDto -> {
                if(incompletedCoursesDto.getDesignCredit()>0){
                    abeekRecommend.add(incompletedCoursesDto);
                }
            }
        );
    }

    private static void printLog(List<IncompletedCoursesDto> incompletedCoursesDtoList){
        log.info("dto list size = {}",incompletedCoursesDtoList.size());
        for (IncompletedCoursesDto incompletedCoursesDto : incompletedCoursesDtoList) {
            log.info("courseName = {}",incompletedCoursesDto.getCourseName());
        }
    }
}
