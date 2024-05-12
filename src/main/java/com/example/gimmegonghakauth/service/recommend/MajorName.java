package com.example.gimmegonghakauth.service.recommend;

public enum MajorName {
    ELEC_INFO("전자정보통신공학과"),
    COMPUTER("컴퓨터공학과");

    private final String name;
    MajorName(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }
}
