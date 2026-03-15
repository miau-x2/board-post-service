package com.example.board.post.commons.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CategoryErrorCode implements ApiCode {
    DEPTH_EXCEEDED("POST_CATEGORY_400_001", "카테고리 최대 깊이(1)를 초과했습니다.", HttpStatus.BAD_REQUEST),
    PARENT_CATEGORY_NOT_FOUND("POST_CATEGORY_404_001", "존재하지 않는 상위 카테고리입니다.", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("POST_CATEGORY_404_002", "존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND),
    NAME_DUPLICATED("POST_CATEGORY_409_001", "이미 사용중인 카테고리 이름입니다.", HttpStatus.CONFLICT),
    SLUG_DUPLICATED("POST_CATEGORY_409_002", "이미 사용중인 슬러그입니다.", HttpStatus.CONFLICT)
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    CategoryErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
