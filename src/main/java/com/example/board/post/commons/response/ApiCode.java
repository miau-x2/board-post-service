package com.example.board.post.commons.response;

import org.springframework.http.HttpStatus;

public interface ApiCode {
    String getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}

