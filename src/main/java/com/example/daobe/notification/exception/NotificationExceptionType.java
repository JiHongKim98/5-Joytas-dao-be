package com.example.daobe.notification.exception;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum NotificationExceptionType implements BaseExceptionType {
    NOT_EXIST_NOTIFICATION("존재하지 않는 알림입니다.", HttpStatus.NOT_FOUND),
    IS_NOT_OWN_NOTIFICATION("다른 사용자의 알림입니다.", HttpStatus.FORBIDDEN),
    DOMAIN_INFO_IS_NULL("도메인 정보는 null 값일 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    NON_MATCH_DOMAIN_EVENT_TYPE("일치하는 도메인 타입이 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UN_SUPPORT_DOMAIN_EVENT_TYPE("지원하지 않는 도메인 이벤트 타입입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    JSON_RESPONSE_SERIALIZATION_ERROR("응답을 JSON 으로 변환하지 못했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String message;
    private final HttpStatus status;

    NotificationExceptionType(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus status() {
        return status;
    }
}
