package com.example.board.post.categories.controller.dto;

import com.example.board.post.categories.service.result.CategoryHeaderMenuItem;

import java.util.List;

public record CategoryHeaderMenuResponse(List<CategoryHeaderMenuItem> roots) {
}
