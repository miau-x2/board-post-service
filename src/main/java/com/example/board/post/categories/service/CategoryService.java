package com.example.board.post.categories.service;

import com.example.board.post.categories.controller.dto.CategoryHeaderMenuResponse;
import com.example.board.post.categories.service.command.CategoryAddCommand;
import com.example.board.post.categories.service.result.AddCategoryResult;

public interface CategoryService {
    AddCategoryResult addCategory(CategoryAddCommand command);
    CategoryHeaderMenuResponse getCategoryHeaderMenu();
}
