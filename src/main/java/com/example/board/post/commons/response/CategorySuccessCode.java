package com.example.board.post.commons.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CategorySuccessCode implements ApiCode {
    CATEGORY_CREATED("POST_CATEGORY_201_001", "카테고리 생성 완료", HttpStatus.CREATED),
    CATEGORY_HEADER_MENU_FOUND("POST_CATEGORY_200_001", "카테고리 헤더 메뉴 조회 완료", HttpStatus.OK)
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    CategorySuccessCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
