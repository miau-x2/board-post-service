package com.example.board.post.categories.service.impl;

import com.example.board.post.categories.entity.Category;
import com.example.board.post.categories.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryTxService {
    private final CategoryRepository categoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCategory(Category category) {
        categoryRepository.saveAndFlush(category);
    }
}
