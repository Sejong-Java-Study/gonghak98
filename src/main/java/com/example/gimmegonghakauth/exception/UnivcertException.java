package com.example.gimmegonghakauth.exception;


import com.example.gimmegonghakauth.constant.UnivcertErrorMessageConst;
import org.springframework.web.client.HttpClientErrorException;

public class UnivcertException {

    public String handleBadRequestException(HttpClientErrorException.BadRequest e,
        String type) {
        String body = e.getResponseBodyAsString();
        String errorMessage;
        if (e.getStatusCode().value() == 400) {
            switch (type) {
                case "이메일 발송":
                    if (body.contains("이미 완료된 요청입니다.")) {
                        errorMessage = UnivcertErrorMessageConst.SEND_MAIL_COMPLETED.getErrorMessage();
                        break;
                    } else if (body.contains("대학과 일치하지 않는 메일 도메인입니다.")) {
                        errorMessage = UnivcertErrorMessageConst.SEND_MAIL_SEJONG.getErrorMessage();
                        break;
                    } else {
                        e.printStackTrace();
                        errorMessage = UnivcertErrorMessageConst.UNEXPECTED_ERROR.getErrorMessage();
                        break;
                    }
                case "이메일 코드 인증":
                    if (body.contains("이미 완료된 요청입니다.")) {
                        errorMessage = UnivcertErrorMessageConst.VERIFY_CODE_COMPLETED.getErrorMessage();
                        break;
                    } else if (body.contains("대학과 일치하지 않는 메일 도메인입니다.")) {
                        errorMessage = UnivcertErrorMessageConst.VERIFY_CODE_SEJONG.getErrorMessage();
                        break;

                    } else {
                        e.printStackTrace();
                        errorMessage = UnivcertErrorMessageConst.UNEXPECTED_ERROR.getErrorMessage();
                        break;
                    }
                case "인증 확인":
                    if (body.contains("인증 요청 이력이 존재하지 않습니다.")) {
                        errorMessage = UnivcertErrorMessageConst.VERIFY_STATUS_NULL.getErrorMessage();
                        break;
                    } else {
                        e.printStackTrace();
                        errorMessage = UnivcertErrorMessageConst.UNEXPECTED_ERROR.getErrorMessage();
                        break;
                    }
                case "인증 초기화":
                    if (body.contains("인증 요청 이력이 존재하지 않습니다.")) {
                        errorMessage = UnivcertErrorMessageConst.CLEAR_CERTIFICATION_NULL.getErrorMessage();
                        break;
                    } else {
                        e.printStackTrace();
                        errorMessage = UnivcertErrorMessageConst.UNEXPECTED_ERROR.getErrorMessage();
                        break;
                    }
                default:
                    errorMessage = UnivcertErrorMessageConst.UNEXPECTED_ERROR.getErrorMessage();
                    break;
            }
            return errorMessage;
        } else {
            e.printStackTrace();
            return UnivcertErrorMessageConst.UNEXPECTED_ERROR.getErrorMessage();
        }
    }

}
