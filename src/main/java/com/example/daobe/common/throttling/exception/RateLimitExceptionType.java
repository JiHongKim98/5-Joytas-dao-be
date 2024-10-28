package com.example.daobe.common.throttling;

import com.example.daobe.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum RateLimitExceptionType implements BaseExceptionType {
    THROTTLING_EXCEPTION("잠시후 다시 시도해주세요.", HttpStatus.TOO_MANY_REQUESTS),
    RATE_LIMIT_USER_ID_ERROR_MESSAGE("사용자 식별자를 가져올 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String message;
    private final HttpStatus status;

    RateLimitExceptionType(String message, HttpStatus status) {
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
