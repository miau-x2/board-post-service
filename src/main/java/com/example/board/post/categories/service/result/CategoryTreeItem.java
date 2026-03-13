package com.example.board.post.categories.service.result;

import java.util.List;

public record CategoryTreeItem(
        Long id,
        String name,
        String slug,
        int displayOrder,
        List<CategoryTreeItem> children) {
}
