package com.cinnamonmiracle.catalog.entity;

import com.cinnamonmiracle.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Port of models/Category.js */
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @NotBlank(message = "Category ID is required")
    @Column(nullable = false, unique = true)
    private String categoryId;

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    @Column(nullable = false, unique = true)
    private String categoryName;

    @NotBlank(message = "Reason is required")
    @Column(nullable = false)
    private String reason;

    @NotBlank(message = "Created by is required")
    @Column(nullable = false)
    private String createdBy;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
