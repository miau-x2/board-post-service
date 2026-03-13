package com.example.board.post.categories.service.impl;

import com.example.board.post.categories.controller.dto.CategoryHeaderMenuResponse;
import com.example.board.post.categories.controller.dto.CategoryTreeResponse;
import com.example.board.post.categories.entity.Category;
import com.example.board.post.categories.repository.CategoryRepository;
import com.example.board.post.categories.service.CategoryService;
import com.example.board.post.categories.service.command.CategoryAddCommand;
import com.example.board.post.categories.service.result.AddCategoryResult;
import com.example.board.post.categories.service.result.CategoryHeaderMenuItem;
import com.example.board.post.categories.service.result.CategoryTreeItem;
import com.example.board.post.categories.service.result.CategoryTreeNode;
import com.example.board.post.commons.exception.UnhandledDataIntegrityViolationException;
import com.example.board.post.commons.utils.DatabaseConstraintName;
import com.example.board.post.commons.utils.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public CategoryTreeResponse getCategoryTree() {
        var categories = categoryRepository.findAllByOrderByParentIdAscDisplayOrderAscIdAsc();
        var roots = toTreeItems(buildRoots(categories));

        return new CategoryTreeResponse(roots);
    }

    @Override
    public CategoryHeaderMenuResponse getCategoryHeaderMenu() {
        var categories = categoryRepository.findAllByOrderByParentIdAscDisplayOrderAscIdAsc();
        var roots = toHeaderMenuItems(buildRoots(categories));

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

    private List<CategoryTreeNode> buildRoots(List<Category> categories) {
        var map = new HashMap<Long, CategoryTreeNode>();
        var roots = new ArrayList<CategoryTreeNode>();

        for (var category : categories) {
            map.put(category.getId(), new CategoryTreeNode(
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

        return roots;
    }

    private List<CategoryTreeItem> toTreeItems(List<CategoryTreeNode> roots) {
        return roots.stream()
                .map(node -> new CategoryTreeItem(
                        node.id(),
                        node.name(),
                        node.slug(),
                        node.displayOrder(),
                        toTreeItems(node.children())
                ))
                .toList();
    }

    private List<CategoryHeaderMenuItem> toHeaderMenuItems(List<CategoryTreeNode> roots) {
        return roots.stream()
                .map(node -> new CategoryHeaderMenuItem(
                        node.id(),
                        node.name(),
                        node.slug(),
                        node.displayOrder(),
                        toHeaderMenuItems(node.children())
                ))
                .toList();
    }
}
