package com.example.gimmegonghakauth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
public class GonghakCoursesByMajorDto {
    private final Long courseId;
    private final String courseName;
    private final int year;
    private final String courseCategory;
    private final String passCategory;
    private final double designCredit;
    private final int credit;
}
