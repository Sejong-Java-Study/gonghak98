package com.example.gimmegonghakauth.service.recommend;

import com.example.gimmegonghakauth.user.domain.UserDomain;
import com.example.gimmegonghakauth.dto.GonghakRecommendCoursesDto;

public interface GonghakRecommendService {
    GonghakRecommendCoursesDto createRecommendCourses(UserDomain userDomain);
}
