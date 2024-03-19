package com.example.gimmegonghakauth.service;

import com.example.gimmegonghakauth.constant.UnivCertTypeConst;
import com.example.gimmegonghakauth.constant.UnivcertErrorMessageConst;
import com.example.gimmegonghakauth.constant.UnivcertUrlConst;
import com.example.gimmegonghakauth.exception.UnivcertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailVerificationService {

    @Value("${univcert.apikey}")
    private String apiKey;

    private final UnivcertException univcertException = new UnivcertException();


    public String sendVerificationEmail(String email, String univName, boolean univCheck) {
        // API 호출을 위한 RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();
        // API 호출을 위한 요청 데이터 설정
        String apiUrl = UnivcertUrlConst.SEND_MAIL.getUrl();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문 생성
        String requestJson =
            "{\"key\":\"" + apiKey + "\",\"email\":\"" + email + "\",\"univName\":\"" + univName
                + "\",\"univ_check\":" + univCheck + "}";

        // HttpEntity를 사용하여 요청 헤더와 본문을 결합
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        // API 호출 및 응답 처리
        try {
            String response = restTemplate.postForObject(apiUrl, entity, String.class);
            return responseMessage(response, UnivCertTypeConst.SEND_MAIL.getType());
        } catch (HttpClientErrorException.BadRequest e) { // HttpClientErrorException 처리
            return univcertException.handleBadRequestException(e,
                UnivCertTypeConst.SEND_MAIL.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return UnivcertErrorMessageConst.UNEXPECTED_ERROR.getErrorMessage();
        }
    }

    public String verifyEmailCode(String email, String univName, int code) {
        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = UnivcertUrlConst.VERIFY_CODE.getUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson =
            "{\"key\":\"" + apiKey + "\",\"email\":\"" + email + "\",\"univName\":\"" + univName
                + "\",\"code\":" + code + "}";

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        try {
            String response = restTemplate.postForObject(apiUrl, entity, String.class);
            return responseMessage(response, UnivCertTypeConst.VERIFY_CODE.getType());

        } catch (HttpClientErrorException.BadRequest e) { // HttpClientErrorException 처리
            return univcertException.handleBadRequestException(e,
                UnivCertTypeConst.VERIFY_CODE.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return UnivcertErrorMessageConst.UNEXPECTED_ERROR.getErrorMessage();
        }
    }
    private String responseMessage(String response, String type) {
        String message = "에러 발생!";
        switch (type) {
            case "이메일 발송":
                message = response.contains("\"success\":true")
                    ? UnivcertErrorMessageConst.SEND_MAIL_SUCCESS.getErrorMessage()
                    : UnivcertErrorMessageConst.SEND_MAIL_FAIL.getErrorMessage();
                break;
            case "이메일 코드 인증":
                message = response.contains("\"success\":true")
                    ? UnivcertErrorMessageConst.VERIFY_CODE_SUCCESS.getErrorMessage()
                    : UnivcertErrorMessageConst.VERIFY_CODE_FAIL.getErrorMessage();
                break;
            case "인증 확인":
                message = response.contains("\"success\":true")
                    ? UnivcertErrorMessageConst.VERIFY_STATUS_SUCCESS.getErrorMessage()
                    : UnivcertErrorMessageConst.VERIFY_STATUS_FAIL.getErrorMessage();
                break;
            case "인증 초기화":
                message = response.contains("\"success\":true")
                    ? UnivcertErrorMessageConst.CLEAR_CERTIFICATION_SUCCESS.getErrorMessage()
                    : UnivcertErrorMessageConst.CLEAR_CERTIFICATION_FAIL.getErrorMessage();
                break;
        }
        return message;
    }
}
