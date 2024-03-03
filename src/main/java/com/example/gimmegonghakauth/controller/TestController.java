package com.example.gimmegonghakauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test0() {
        return "테스트트트트";
    }

    @GetMapping("/api/test")
    public String test() {
        return "SpringBoot 실행 성공";
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello";
    }
    
    // Github Actions 파이프라인 테스트
}
