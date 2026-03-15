package com.example.board.post.categories.service.result;

public sealed interface AddCategoryResult {
    record Success() implements AddCategoryResult {}
    record DepthExceeded() implements AddCategoryResult {}
    record ParentNotFound() implements AddCategoryResult {}
    record NameAlreadyExists() implements AddCategoryResult {}
    record SlugAlreadyExists() implements AddCategoryResult {}
}
