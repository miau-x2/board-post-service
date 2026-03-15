package com.example.board.post.categories.service.result;

public sealed interface UpdateCategoryResult {
    record Success() implements UpdateCategoryResult {}
    record NotFound() implements UpdateCategoryResult {}
}
