package com.example.gimmegonghakauth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "course")
public class CoursesDomain {

    @Id
    @Column(name = "course_id")
    private Long courseId;

    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "credit")
    private int credit;
}