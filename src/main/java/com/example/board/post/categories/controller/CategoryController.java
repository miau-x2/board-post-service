package com.example.board.post.categories.controller;

import com.example.board.post.categories.controller.dto.CategoryAddRequest;
import com.example.board.post.categories.controller.dto.CategoryHeaderMenuResponse;
import com.example.board.post.categories.controller.dto.CategoryTreeResponse;
import com.example.board.post.categories.service.CategoryService;
import com.example.board.post.categories.service.command.CategoryAddCommand;
import com.example.board.post.categories.service.result.AddCategoryResult;
import com.example.board.post.commons.response.ApiResponse;
import com.example.board.post.commons.response.CategoryErrorCode;
import com.example.board.post.commons.response.CategorySuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    public ResponseEntity<ApiResponse<Void>> addCategory(@Valid @RequestBody CategoryAddRequest request) {
        var result = categoryService.addCategory(new CategoryAddCommand(request.parentId(), request.name(), request.slug(), request.displayOrder()));
        return switch (result) {
            case AddCategoryResult.Success _ -> {
                var code = CategorySuccessCode.CATEGORY_CREATED;
                yield ResponseEntity
                        .status(code.getHttpStatus())
                        .body(ApiResponse.success(code));
            }
            case AddCategoryResult.DepthExceeded _ -> {
                var code = CategoryErrorCode.DEPTH_EXCEEDED;
                yield ResponseEntity
                        .status(code.getHttpStatus())
                        .body(ApiResponse.error(code));
            }
            case AddCategoryResult.ParentNotFound _ -> {
                var code = CategoryErrorCode.PARENT_NOT_FOUND;
                yield ResponseEntity
                        .status(code.getHttpStatus())
                        .body(ApiResponse.error(code));
            }
            case AddCategoryResult.NameAlreadyExists _ -> {
                var code = CategoryErrorCode.NAME_DUPLICATED;
                yield ResponseEntity
                        .status(code.getHttpStatus())
                        .body(ApiResponse.error(code));
            }

            case AddCategoryResult.SlugAlreadyExists _ -> {
                var code = CategoryErrorCode.SLUG_DUPLICATED;
                yield ResponseEntity
                        .status(code.getHttpStatus())
                        .body(ApiResponse.error(code));
            }
        };
    }

    @GetMapping("/admin/categories/tree")
    public ResponseEntity<ApiResponse<CategoryTreeResponse>> getCategoryTree() {
        var code = CategorySuccessCode.CATEGORY_TREE_FOUND;
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(code, categoryService.getCategoryTree()));
    }

    @GetMapping("/header-menu")
    public ResponseEntity<ApiResponse<CategoryHeaderMenuResponse>> getCategoryHeaderMenu() {
        var code = CategorySuccessCode.CATEGORY_HEADER_MENU_FOUND;
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(code, categoryService.getCategoryHeaderMenu()));
    }
}
