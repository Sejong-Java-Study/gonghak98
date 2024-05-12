package com.example.gimmegonghakauth.controller;

import com.example.gimmegonghakauth.domain.CompletedCoursesDomain;
import com.example.gimmegonghakauth.exception.FileException;
import com.example.gimmegonghakauth.service.CompletedCoursesService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/excel")
public class CompletedCoursesController {

    private final CompletedCoursesService excelService;

    public CompletedCoursesController(CompletedCoursesService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/excel")
    public String excel(Model model, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<CompletedCoursesDomain> dataList = excelService.getExcelList(userDetails);
        model.addAttribute("datas",dataList);
        return "excel/excelList";
    }

    @PostMapping("/excel/read")
    public String readExcel(@RequestParam("file") MultipartFile file, Model model,
        Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<CompletedCoursesDomain> beforeDataList = excelService.getExcelList(userDetails);
        model.addAttribute("datas",beforeDataList);

        try {
            excelService.extractExcelFile(file, userDetails);
            List<CompletedCoursesDomain> afterDataList = excelService.getExcelList(userDetails);
            model.addAttribute("datas",afterDataList);
            return "excel/excelList";
        } catch (IOException | FileException e) {
            model.addAttribute("error", e.getMessage());
            return "excel/excelList";
        }
    }
}
