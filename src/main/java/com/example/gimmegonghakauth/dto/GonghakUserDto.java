package com.example.gimmegonghakauth.dto;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
public class GonghakUserDto {

    private final Map<String, Double> userStatus;
}
