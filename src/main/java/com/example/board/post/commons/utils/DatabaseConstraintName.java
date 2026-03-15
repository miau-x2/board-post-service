package com.example.board.post.commons.utils;

public final class DatabaseConstraintName {
    private DatabaseConstraintName() {}

    public static final class Category {
        private Category() {}
        public static final String NAME = "category.uk_category_parent_name";
        public static final String SLUG = "category.uk_category_slug";
        public static final String PARENT = "category.fk_category_parent";
    }
}
