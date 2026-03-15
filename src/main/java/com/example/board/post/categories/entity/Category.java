package com.example.board.post.categories.entity;

import com.example.board.post.commons.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE category SET deleted_at = NOW() WHERE category_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @NotBlank
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;

    @NotBlank
    @Size(min = 1, max = 50)
    @Column(name = "slug", unique = true)
    private String slug;

    @PositiveOrZero
    @Max(255)
    @Column(name = "display_order", nullable = false, columnDefinition = "TINYINT UNSIGNED")
    private int displayOrder;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private Category(Long parentId, String name, String slug, int displayOrder, LocalDateTime deletedAt) {
        this.parentId = parentId;
        this.name = name;
        this.slug = slug;
        this.displayOrder = displayOrder;
        this.deletedAt = deletedAt;
    }

    public static Category root(String name, String slug, int displayOrder) {
        return new Category(null, name, slug, displayOrder, null);
    }

    public static Category child(Long parentId, String name, String slug, int displayOrder) {
        return new Category(parentId, name, slug, displayOrder, null);
    }

    public void updateDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
