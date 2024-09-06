package com.example.gimmegonghakauth.constant;

public enum AbeekTypeConst {

    PROFESSIONAL_NON_MAJOR("전문교양"),
    NON_MAJOR("교양"),
    MSC("MSC"),
    MAJOR("전공"),
    DESIGN("설계"),
    MINIMUM_CERTI("최소 이수학점"),
    BSM("BSM");

    private final String typeMessage;

    AbeekTypeConst(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getTypeMessage() {
        return typeMessage;
    }
}
