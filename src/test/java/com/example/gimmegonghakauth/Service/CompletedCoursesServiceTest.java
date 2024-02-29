package com.example.gimmegonghakauth.Service;

import com.example.gimmegonghakauth.exception.FileException;
import com.example.gimmegonghakauth.service.CompletedCoursesService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Nested
@DisplayName("엑셀 업로드 테스트")
class CompletedCoursesServiceTest {

    @InjectMocks
    private CompletedCoursesService excelService;

    @Mock
    private Workbook workbook;

    @DisplayName("빈 파일 업로드")
    @Test
    void testValidateExcelFileEmptyFile() {
        // 빈 파일(MockMultipartFile) 생성
        MockMultipartFile emptyFile = new MockMultipartFile("file", "test.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new byte[0]);
        // FileException이 발생하는지 확인
        assertThrows(FileException.class, () -> excelService.validateExcelFile(emptyFile, "xlsx"));
    }

    @DisplayName("확장자가 다른 파일 업로드")
    @Test
    void testValidateExcelFileInvalidExtension() {
        // 확장자가 잘못된 파일(MockMultipartFile) 생성
        MockMultipartFile invalidExtensionFile = new MockMultipartFile("file", "test.txt",
            "text/plain", "Some content".getBytes());
        // FileException이 발생하는지 확인
        assertThrows(FileException.class,
            () -> excelService.validateExcelFile(invalidExtensionFile, "txt"));
    }

    @DisplayName("빈 엑셀파일 업로드")
    @Test
    void testValidateExcelContentEmptyWorksheet() {
        // 빈 시트 생성
        Sheet emptySheet = workbook.createSheet("TestSheet");

        // 필요한 호출에 대해 불필요한 스텁 경고를 무시하도록 lenient 모드로 설정
        lenient().when(workbook.getSheetAt(0)).thenReturn(emptySheet);

        // FileException이 발생하는지 확인
        assertThrows(FileException.class,
            () -> excelService.validateExcelContent(emptySheet, null));
    }

    @DisplayName("이수성적 파일이 아닌 엑셀파일 업로드")
    @Test
    void testValidateExcelContentInvalidHeader() {
        // 엑셀 워크북 목 객체 생성
        Workbook workbookMock = mock(Workbook.class);

        // 엑셀 시트 목 객체 생성
        Sheet sheetWithInvalidHeader = mock(Sheet.class);
        Row headerRow = mock(Row.class);
        Cell headerCell = mock(Cell.class);

        // lenient()를 사용하여 불필요한 스텁 경고를 무시하도록 설정
        lenient().when(sheetWithInvalidHeader.createRow(0)).thenReturn(headerRow);
        lenient().when(headerRow.getCell(0)).thenReturn(headerCell);
        lenient().when(headerCell.getStringCellValue()).thenReturn("기이수성적");

        // 첫 번째 행의 내용이 "기이수성적"이 아닌 경우를 테스트하기 위해 다른 내용으로 설정
        Row contentRow = mock(Row.class);
        Cell contentCell = mock(Cell.class);
        lenient().when(sheetWithInvalidHeader.getRow(1)).thenReturn(contentRow);
        lenient().when(contentRow.getCell(0)).thenReturn(contentCell);
        lenient().when(contentCell.getStringCellValue()).thenReturn("다른내용");

        // workbook.getSheetAt(0) 호출 시에 sheetWithInvalidHeader를 반환하도록 설정
        when(workbookMock.getSheetAt(0)).thenReturn(sheetWithInvalidHeader);

        // FileException이 발생하는지 확인
        assertThrows(FileException.class,
            () -> excelService.validateExcelContent(workbookMock.getSheetAt(0), null));
    }

}
