package com.example.gimmegonghakauth.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelDomain {

    private Integer studentId; //학번
    private Integer courseId; //학수번호
    private Integer year; //수강년도
    private Integer semester; //수강학기
}
