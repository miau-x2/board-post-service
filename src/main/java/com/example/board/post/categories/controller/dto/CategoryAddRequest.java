package com.example.board.post.categories.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryAddRequest(
        Long parentId,
        @NotBlank
        @Size(min = 1, max = 50)
        String name,
        @NotBlank
        @Size(min = 1, max = 50)
        String slug,
        @Max(255)
        int displayOrder) {
}
