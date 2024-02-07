package com.example.gimmegonghakauth.controller;

import com.example.gimmegonghakauth.domain.ExcelDomain;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExcelController {

    @GetMapping("/excel")
    public String main(){
        return "excel";
    }

    @PostMapping("/excel/read")
    public String readExcel(@RequestParam("file")MultipartFile file, Model model)
        throws IOException{

        List<ExcelDomain> dataList = new ArrayList<>();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());


        Workbook workbook = null;
        if(extension.equals("xlsx")){
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")){
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        Sheet worksheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        for(int i=4; i<worksheet.getPhysicalNumberOfRows(); i++){
            Row row = worksheet.getRow(i);
            ExcelDomain data = new ExcelDomain();

            String yearAsString = dataFormatter.formatCellValue(row.getCell(1));
            data.setYear(Integer.parseInt(yearAsString));

            String semsterAsString = dataFormatter.formatCellValue(row.getCell(2));
            data.setSemester(Integer.parseInt(String.valueOf(semsterAsString.charAt(0))));

            String course_idAsString = dataFormatter.formatCellValue(row.getCell(3));
            data.setCourse_id(Integer.parseInt(course_idAsString));

            dataList.add(data);
        }
        model.addAttribute("datas",dataList);

        return "excelList";
    }
}
