package com.example.gimmegonghakauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserJoinDto {

    @NotNull(message = "학번은 필수항목입니다.")
    @Pattern(regexp = "\\d{8}", message = "학번은 8자리 숫자여야 합니다.")
    private String studentId;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

    @NotEmpty(message = "학과는 필수항목입니다.")
    private String major;

    @NotEmpty(message = "이름은 필수항목입니다.")
    private String name;
}
