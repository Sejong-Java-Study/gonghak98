package com.example.gimmegonghakauth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Data
@Entity
@Builder
@AllArgsConstructor
public class CompletedCoursesDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserDomain userDomain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CoursesDomain coursesDomain;

    @NotNull
    @Range(min = 2015,max = 2024)
    private int year;

    @NotNull
    @Range(min = 1, max = 2)
    private int semester;


    public CompletedCoursesDomain() {

    }
}
