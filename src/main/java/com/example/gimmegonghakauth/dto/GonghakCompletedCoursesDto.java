package com.example.gimmegonghakauth.dto;

import com.example.gimmegonghakauth.domain.CoursesDomain;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
public class GonghakCompletedCoursesDto {

    private final List<CoursesDomain> completedCourseIds;
}
