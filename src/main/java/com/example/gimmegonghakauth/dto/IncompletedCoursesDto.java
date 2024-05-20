package com.example.gimmegonghakauth.dto;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import com.example.gimmegonghakauth.constant.CourseCategoryConst;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IncompletedCoursesDto {
    private final String courseName;
    private final CourseCategoryConst courseCategory;
    private final int credit;
    private final double designCredit;
}
