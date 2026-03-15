package com.example.board.post.commons.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CategorySuccessCode implements ApiCode {
    CATEGORY_CREATED("POST_CATEGORY_201_001", "카테고리 생성 완료", HttpStatus.CREATED),
    CATEGORY_TREE_FOUND("POST_CATEGORY_200_001", "카테고리 트리 조회 완료", HttpStatus.OK),
    CATEGORY_HEADER_MENU_FOUND("POST_CATEGORY_200_002", "카테고리 헤더 메뉴 조회 완료", HttpStatus.OK),
    CATEGORY_DISPLAY_ORDER_UPDATED("POST_CATEGORY_204_001", "카테고리 디스플레이 순서 변경 완료", HttpStatus.NO_CONTENT)
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
