package com.example.gimmegonghakauth.dto;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
public class GonghakResultDto {
    private final Map<AbeekTypeConst, Double> userResultRatio;
}
