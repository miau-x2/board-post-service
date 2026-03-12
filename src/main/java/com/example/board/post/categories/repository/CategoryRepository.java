package com.example.board.post.categories.repository;

import com.example.board.post.categories.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByOrderByParentIdAscDisplayOrderAsc();
}
