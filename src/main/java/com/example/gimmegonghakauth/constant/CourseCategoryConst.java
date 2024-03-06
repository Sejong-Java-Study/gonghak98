package com.example.gimmegonghakauth.constant;

public enum CourseCategoryConst {

    MAJOR_SELECTIVE("전선"),
    MAJOR_REQUIRED("전필"),
    PROFESSIONAL_NON_MAJOR("전문교양"),
    MSC("MSC");

    private final String typeMessage;

    CourseCategoryConst(String typeMessage) {
        this.typeMessage = typeMessage;
    }
}