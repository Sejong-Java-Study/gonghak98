package com.example.gimmegonghakauth.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.validator.constraints.Range;

@Data
@Entity
@Getter
public class CompletedCoursesDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDomain userDomain; //학번

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CoursesDomain coursesDomain;//학수번호

    @NotNull
    @Range(min = 2015,max = 2024)
    private Integer year; //수강년도

    @NotNull
    @Range(min = 1, max = 2)
    private Integer semester; //수강학기

    @Builder
    public CompletedCoursesDomain(UserDomain userDomain,CoursesDomain coursesDomain, Integer year, Integer semester){
        this.userDomain= userDomain;
        this.coursesDomain = coursesDomain;
        this.year=year;
        this.semester=semester;
     }

    public CompletedCoursesDomain() {

    }
}
