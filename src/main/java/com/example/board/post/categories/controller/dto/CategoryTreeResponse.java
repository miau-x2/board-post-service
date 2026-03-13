package com.example.board.post.categories.controller.dto;

import com.example.board.post.categories.service.result.CategoryTreeItem;

import java.util.List;

public record CategoryTreeResponse(List<CategoryTreeItem> roots) {
}
