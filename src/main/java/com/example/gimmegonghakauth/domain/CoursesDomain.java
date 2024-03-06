package com.example.gimmegonghakauth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Entity
@Data
public class CoursesDomain {
    @Id
    private Long courseId;

    @NotNull
    private String name;
    @NotNull
    private int credit;

    @Builder
    public CoursesDomain(Long courseId, String name, int credit) {
        this.courseId = courseId;
        this.name = name;
        this.credit = credit;
    }

    public CoursesDomain() {

    }
}