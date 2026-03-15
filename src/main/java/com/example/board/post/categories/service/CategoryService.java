package com.example.board.post.categories.service;

import com.example.board.post.categories.controller.dto.CategoryHeaderMenuResponse;
import com.example.board.post.categories.controller.dto.CategoryTreeResponse;
import com.example.board.post.categories.service.command.CategoryAddCommand;
import com.example.board.post.categories.service.result.AddCategoryResult;
import com.example.board.post.categories.service.result.UpdateCategoryResult;

public interface CategoryService {
    AddCategoryResult addCategory(CategoryAddCommand command);
    CategoryTreeResponse getCategoryTree();
    CategoryHeaderMenuResponse getCategoryHeaderMenu();
    UpdateCategoryResult updateDisplayOrder(Long id, int displayOrder);
}
