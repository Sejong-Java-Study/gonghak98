package com.example.gimmegonghakauth.service;

import com.example.gimmegonghakauth.domain.ExcelDomain;
import com.example.gimmegonghakauth.exception.FileException;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {
    public List<ExcelDomain> extractExcelFile(MultipartFile file) throws IOException { //엑셀 데이터 추출
        List<ExcelDomain> dataList = new ArrayList<>();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        validateExcelFile(file, extension); //업로드 파일 검증

        Workbook workbook = creatWorkbook(file, extension);
        Sheet worksheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        validateExcelContent(worksheet, dataFormatter);//엑셀 내용 검증

        for (int i = 4; i < worksheet.getPhysicalNumberOfRows(); i++) { //데이터 추출
            Row row = worksheet.getRow(i);
            ExcelDomain data = new ExcelDomain();

            String yearAsString = dataFormatter.formatCellValue(row.getCell(1));
            data.setYear(Integer.parseInt(yearAsString));

            String semesterAsString = dataFormatter.formatCellValue(row.getCell(2));
            data.setSemester(Integer.parseInt(String.valueOf(semesterAsString.charAt(0))));

            String course_idAsString = dataFormatter.formatCellValue(row.getCell(3));
            data.setCourse_id(Integer.parseInt(course_idAsString));

            dataList.add(data);
        }
        return dataList; //데이터 반환
    }

    //업로드 파일 검증
    public void validateExcelFile(MultipartFile file, String extension) throws FileException {
        if (file.isEmpty()) { // 파일이 비어있으면
            throw new FileException("파일이 비어 있습니다.");
        }
        if (!extension.equals("xlsx") && !extension.equals("xls")) { //엑셀파일이 아니면
            throw new FileException("엑셀 파일만 업로드 해주세요.");
        }
    }

    // 엑셀 내용 검증
    public void validateExcelContent(Sheet workSheet, DataFormatter dataFormatter) throws FileException {
        if (workSheet == null) {
            throw new FileException("엑셀파일이 비어있습니다.");
        }
        Row row = workSheet.getRow(0);
        if (row == null) { //엑셀파일이 비어있으면
            throw new FileException("엑셀파일이 비어있습니다.");
        }
        String data = dataFormatter.formatCellValue(row.getCell(0));
        if (!data.equals("기이수성적")) { //형식이 올바르지 않으면
            throw new FileException("기이수성적 엑셀파일을 업로드 해주세요.");
        }
    }

    //확장자에 맞춰서 workbook 리턴
    private Workbook creatWorkbook(MultipartFile file, String extension) throws IOException {
        Workbook workbook = null;
        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }
        return workbook;
    }

}
