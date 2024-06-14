package com.example.gimmegonghakauth.domain;

import com.example.gimmegonghakauth.constant.CourseCategoryConst;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "gonghak_course")
public class GonghakCoursesDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gonghak_course_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private MajorsDomain majorsDomain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CoursesDomain coursesDomain;

    @NotNull
    @Range(min = 15, max = 24)
    @Column(name = "year")
    private int year;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "course_category_const")
    private CourseCategoryConst courseCategory;

    @NotNull
    @Column(name = "pass_category")
    private String passCategory;

    @NotNull
    @Range(min = 0,max = 6)
    @Column(name = "design_credit")
    private double designCredit;
}
