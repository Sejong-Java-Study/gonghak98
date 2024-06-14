package com.example.gimmegonghakauth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginDto {

    @NotNull @NotBlank
    @Range(min = 10000000)
    private final Long studentId;
}
