package com.cinnamonmiracle.catalog.service;

import com.cinnamonmiracle.catalog.entity.Category;
import com.cinnamonmiracle.catalog.repository.CategoryRepository;
import com.cinnamonmiracle.common.exception.ApiException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Port of controllers/categoryController.js */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Map<String, Object> getCategories(int page, int size) {
        try {
            if (page < 1) {
                page = 1;
            }
            long totalCategories = categoryRepository.count();
            int totalPages = (int) Math.ceil((double) totalCategories / size);

            List<Category> categories = categoryRepository.findAll(PageRequest.of(page - 1, size)).getContent();

            Map<String, Object> pagination = new LinkedHashMap<>();
            pagination.put("currentPage", page);
            pagination.put("totalPages", totalPages);
            pagination.put("totalCategories", totalCategories);
            pagination.put("size", size);
            pagination.put("hasNextPage", page < totalPages);
            pagination.put("hasPrevPage", page > 1);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("categories", categories);
            response.put("pagination", pagination);
            return response;
        } catch (Exception e) {
            throw new ApiException(500, Map.of("message", "Server error", "error",
                    e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    public Category createCategory(Category category, String userId) {
        try {
            if (category.getCreatedBy() == null || category.getCreatedBy().isBlank()) {
                category.setCreatedBy(userId);
            }
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new ApiException(400, Map.of("message", "Invalid data", "error",
                    e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    public Category getCategoryById(String id) {
        try {
            if (!isValidId(id)) {
                throw new ApiException(400, Map.of("message", "Invalid category ID format"));
            }
            return categoryRepository.findById(id)
                    .orElseThrow(() -> new ApiException(404, Map.of("message", "Category not found")));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, Map.of("message", "Server error", "error",
                    e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    public Category updateCategory(String id, Map<String, Object> body) {
        try {
            if (!isValidId(id)) {
                throw new ApiException(400, Map.of("message", "Invalid category ID format"));
            }

            Category category = categoryRepository.findById(id).orElse(null);
            if (category == null) {
                throw new ApiException(404, Map.of("message", "Category not found"));
            }

            if (body.containsKey("categoryId")) {
                category.setCategoryId((String) body.get("categoryId"));
            }
            if (body.containsKey("categoryName")) {
                category.setCategoryName((String) body.get("categoryName"));
            }
            if (body.containsKey("reason")) {
                category.setReason((String) body.get("reason"));
            }
            if (body.containsKey("createdBy")) {
                category.setCreatedBy((String) body.get("createdBy"));
            }

            return categoryRepository.save(category);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(400, Map.of("message", "Invalid data", "error",
                    e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    public Map<String, Object> deleteCategory(String id) {
        try {
            if (!isValidId(id)) {
                throw new ApiException(400, Map.of("message", "Invalid category ID format"));
            }

            Category category = categoryRepository.findById(id).orElse(null);
            if (category == null) {
                throw new ApiException(404, Map.of("message", "Category not found"));
            }

            categoryRepository.deleteById(id);
            return Map.of("message", "Category deleted");
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, Map.of("message", "Server error", "error",
                    e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    private boolean isValidId(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        try {
            java.util.UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
