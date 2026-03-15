package com.example.board.post.categories.service.impl;

import com.example.board.post.categories.controller.dto.CategoryHeaderMenuResponse;
import com.example.board.post.categories.controller.dto.CategoryTreeResponse;
import com.example.board.post.categories.entity.Category;
import com.example.board.post.categories.repository.CategoryRepository;
import com.example.board.post.categories.service.CategoryService;
import com.example.board.post.categories.service.command.CategoryAddCommand;
import com.example.board.post.categories.service.result.*;
import com.example.board.post.commons.exception.UnhandledDataIntegrityViolationException;
import com.example.board.post.commons.utils.DatabaseConstraintName;
import com.example.board.post.commons.utils.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryTxService categoryTxService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public CategoryTreeResponse getCategoryTree() {
        var categories = categoryRepository.findAllByOrderByParentIdAscDisplayOrderAscIdAsc();
        var roots = toTreeItems(buildRoots(categories));
        log.info("관리자 카테고리 목록 조회");

        return new CategoryTreeResponse(roots);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryHeaderMenuResponse getCategoryHeaderMenu() {
        var categories = categoryRepository.findAllByOrderByParentIdAscDisplayOrderAscIdAsc();
        var roots = toHeaderMenuItems(buildRoots(categories));

        return new CategoryHeaderMenuResponse(roots);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UpdateCategoryResult updateDisplayOrder(Long id, int displayOrder) {
        return categoryRepository.findById(id)
                .<UpdateCategoryResult>map(category -> {
                    category.updateDisplayOrder(displayOrder);
                    log.info("카테고리 순서 수정: {}", category.getName());
                    return new UpdateCategoryResult.Success();
                })
                .orElseGet(UpdateCategoryResult.NotFound::new);
    }

    private AddCategoryResult saveCategory(Category category) {
        try {
            categoryTxService.saveCategory(category);
            log.info("카테고리 저장: {}", category.getName());
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
