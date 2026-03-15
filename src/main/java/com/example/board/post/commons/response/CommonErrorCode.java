package com.example.board.post.commons.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode implements ApiCode {
    REQUEST_MALFORMED("POST_COMMON_400_001", "잘못된 요청 형식입니다.", HttpStatus.BAD_REQUEST),
    INPUT_INVALID("POST_COMMON_400_002", "입력값이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED("POST_COMMON_401_001", "토큰이 만료되었습니다", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID("POST_COMMON_401_002", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("POST_COMMON_403_001", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER_ERROR("POST_COMMON_500_001", "현재 요청을 처리할 수 없습니다. 잠시 후 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    CommonErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
