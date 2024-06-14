package com.example.gimmegonghakauth.dto;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GonghakRecommendCoursesDto {
    private final Map<AbeekTypeConst, List<IncompletedCoursesDto>> recommendCoursesByAbeekType = new ConcurrentHashMap<>();
}
