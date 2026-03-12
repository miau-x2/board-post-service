package com.example.board.post.categories.service.impl;

import com.example.board.post.categories.controller.dto.CategoryHeaderMenuResponse;
import com.example.board.post.categories.entity.Category;
import com.example.board.post.categories.repository.CategoryRepository;
import com.example.board.post.categories.service.CategoryService;
import com.example.board.post.categories.service.command.CategoryAddCommand;
import com.example.board.post.categories.service.result.AddCategoryResult;
import com.example.board.post.categories.service.result.CategoryHeaderMenuItem;
import com.example.board.post.commons.exception.UnhandledDataIntegrityViolationException;
import com.example.board.post.commons.utils.DatabaseConstraintName;
import com.example.board.post.commons.utils.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryTxService categoryTxService;

    @Override
    public AddCategoryResult addCategory(CategoryAddCommand command) {
        if(command.parentId() == null) {
            return saveCategory(Category.root(command.name(), command.slug(), command.displayOrder()));
        }
        var optParent = categoryRepository.findById(command.parentId());
        if(optParent.isEmpty()) {
            return new AddCategoryResult.ParentNotFound();
        }
        var parent = optParent.get();
        // 요구사항: 카테고리 depth = 1
        if(parent.getParentId() != null) {
            return new AddCategoryResult.DepthExceeded();
        }
        return saveCategory(Category.child(parent.getId(), command.name(), command.slug(), command.displayOrder()));
    }

    @Override
    public CategoryHeaderMenuResponse getCategoryHeaderMenu() {
        var categories = categoryRepository.findAllByOrderByParentIdAscDisplayOrderAsc();
        var map = new HashMap<Long, CategoryHeaderMenuItem>();
        var roots = new ArrayList<CategoryHeaderMenuItem>();

        for(var category: categories) {
            map.put(category.getId(), new CategoryHeaderMenuItem(
                    category.getId(),
                    category.getName(),
                    category.getSlug(),
                    category.getDisplayOrder(),
                    new ArrayList<>()
            ));
        }

        for (var category : categories) {
            var item = map.get(category.getId());
            if(category.getParentId() == null) {
                roots.add(item);
            }
            else {
                var parent = map.get(category.getParentId());
                if(parent != null) {
                    parent.children().add(item);
                }
            }
        }

        return new CategoryHeaderMenuResponse(roots);
    }

    private AddCategoryResult saveCategory(Category category) {
        try {
            categoryTxService.saveCategory(category);
            return new AddCategoryResult.Success();
        } catch (DataIntegrityViolationException e) {
            var constraintName = ExceptionUtils.findConstraintName(e);
            if(DatabaseConstraintName.Category.NAME.equals(constraintName)) {
                return new AddCategoryResult.NameAlreadyExists();
            }
            if(DatabaseConstraintName.Category.SLUG.equals(constraintName)) {
                return new AddCategoryResult.SlugAlreadyExists();
            }
            if(DatabaseConstraintName.Category.PARENT.equals(constraintName)) {
                return new AddCategoryResult.ParentNotFound();
            }
            throw new UnhandledDataIntegrityViolationException(e);
        }
    }
}
