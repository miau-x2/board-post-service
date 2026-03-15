package com.example.board.post.categories.service.command;

public record CategoryAddCommand(Long parentId, String name, String slug, int displayOrder) {
}
