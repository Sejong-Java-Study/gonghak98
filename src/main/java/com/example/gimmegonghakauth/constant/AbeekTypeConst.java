package com.example.gimmegonghakauth.constant;

public enum AbeekTypeConst {

    PROFESSIONAL_NON_MAJOR("전문교양"),
    MSC("MSC"),
    MAJOR("전공학점"),
    DESIGN("설계학점"),
    MINIMUM_CERTI("최소인증학점");

    private final String typeMessage;

    AbeekTypeConst(String typeMessage) {

        this.typeMessage = typeMessage;
    }
}
