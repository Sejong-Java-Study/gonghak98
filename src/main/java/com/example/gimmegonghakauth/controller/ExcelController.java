package com.example.gimmegonghakauth.controller;

import com.example.gimmegonghakauth.domain.ExcelDomain;
import com.example.gimmegonghakauth.exception.FileException;
import com.example.gimmegonghakauth.service.ExcelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class ExcelController {

    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/excel")
    public String main() {
        return "excel";
    }

    @PostMapping("/excel/read")
    public String readExcel(@RequestParam("file") MultipartFile file, Model model) {
        try {
            List<ExcelDomain> dataList = excelService.extractExcelFile(file);
            model.addAttribute("datas", dataList);
            return "excelList";
        } catch (IOException | FileException e) {
            model.addAttribute("error", e.getMessage());
            return "excel";
        }
    }
}
