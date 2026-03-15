package com.example.board.post.categories.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record DisplayOrderUpdateRequest(
        @NotNull
        Long id,
        @Max(255)
        int displayOrder) {
}
