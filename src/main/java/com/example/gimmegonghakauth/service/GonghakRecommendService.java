package com.example.gimmegonghakauth.service;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.constant.CourseCategoryConst;
import com.example.gimmegonghakauth.dao.GonghakRepository;
import com.example.gimmegonghakauth.domain.UserDomain;
import com.example.gimmegonghakauth.dto.GonghakRecommendCoursesDto;
import com.example.gimmegonghakauth.dto.IncompletedCoursesDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.theme.FixedThemeResolver;

@Service
@RequiredArgsConstructor
@Slf4j
public class GonghakRecommendService {

    private final GonghakRepository gonghakRepository;

    //리팩토링 필요
    public GonghakRecommendCoursesDto createRecommendCourses(UserDomain userDomain){
        GonghakRecommendCoursesDto gonghakRecommendCoursesDto = new GonghakRecommendCoursesDto();

        List<IncompletedCoursesDto> majorSelective = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
            CourseCategoryConst.MAJOR_SELECTIVE, userDomain.getStudentId(), userDomain.getMajorsDomain()
        );
        printLog(majorSelective);
        List<IncompletedCoursesDto> majorRequired = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
            CourseCategoryConst.MAJOR_REQUIRED, userDomain.getStudentId(), userDomain.getMajorsDomain()
        );
        printLog(majorRequired);
        List<IncompletedCoursesDto> nonMajor = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
            CourseCategoryConst.PROFESSIONAL_NON_MAJOR, userDomain.getStudentId(), userDomain.getMajorsDomain()
        );
        printLog(nonMajor);
        List<IncompletedCoursesDto> msc = gonghakRepository.findUserCoursesByMajorByGonghakCoursesWithoutCompleteCourses(
            CourseCategoryConst.MSC, userDomain.getStudentId(), userDomain.getMajorsDomain()
        );
        printLog(msc);

        Map<AbeekTypeConst, List<IncompletedCoursesDto>> coursesByAbeekTypeWithoutCompleteCourses = gonghakRecommendCoursesDto.getRecommendCoursesByAbeekType();
        Arrays.stream(AbeekTypeConst.values()).forEach(
            abeekType -> {
                List<IncompletedCoursesDto> abeekRecommend = new ArrayList<>();
                switch (abeekType){
                    case MSC :
                        abeekRecommend.addAll(msc); break;
                    case MAJOR :
                        abeekRecommend.addAll(majorRequired);
                        abeekRecommend.addAll(majorSelective); break;
                    case DESIGN:
                        abeekRecommend.addAll(majorRequired);
                        abeekRecommend.addAll(majorSelective); break;
                    case PROFESSIONAL_NON_MAJOR:
                        abeekRecommend.addAll(nonMajor); break;
                    case MINIMUM_CERTI:
                        abeekRecommend.addAll(msc);
                        abeekRecommend.addAll(majorRequired);
                        abeekRecommend.addAll(majorSelective);
                        abeekRecommend.addAll(nonMajor); break;
                }
                coursesByAbeekTypeWithoutCompleteCourses.put(abeekType,abeekRecommend);
            }
        );

        return gonghakRecommendCoursesDto;
    }

    private void printLog(List<IncompletedCoursesDto> incompletedCoursesDtoList){
        log.info("-----------");
        log.info("dto list size = {}",incompletedCoursesDtoList.size());
        for (IncompletedCoursesDto incompletedCoursesDto : incompletedCoursesDtoList) {
            log.info("courseName = {}",incompletedCoursesDto.getCourseName());
        }
    }
}
