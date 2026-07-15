package com.cinnamonmiracle.catalog.controller;

import com.cinnamonmiracle.catalog.entity.Category;
import com.cinnamonmiracle.catalog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** routes/categoryRoutes.js (all routes protected by authMiddleware). */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/page/{page}/size/{size}")
    public ResponseEntity<Map<String, Object>> getCategories(
            @PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok(categoryService.getCategories(page, size));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestBody Category category,
            @RequestAttribute(name = "userId", required = false) String userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(category, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable String id, @RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(categoryService.updateCategory(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
