package com.example.gimmegonghakauth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Entity
@Data
@AllArgsConstructor
@Builder
public class GonghakCoursesDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private MajorsDomain majorsDomain;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CoursesDomain coursesDomain;

    @NotNull
    @Range(min = 15, max = 24)
    private int year;
    @NotNull
    private String courseCategory;
    @NotNull
    private String passCategory;
    @NotNull
    @Range(min = 0,max = 6)
    private double designCredit;

    public GonghakCoursesDomain() {

    }
}
