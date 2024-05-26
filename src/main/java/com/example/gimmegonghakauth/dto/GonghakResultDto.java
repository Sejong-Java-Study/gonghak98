package com.example.gimmegonghakauth.dto;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GonghakResultDto {
    @Getter
    @RequiredArgsConstructor
    public static class ResultPointDto {
        private final Double userPoint;
        private final int standardPoint;
    }

    private final Map<AbeekTypeConst, ResultPointDto> userResultRatio;
}
