package com.elice.showpet.controller;


import com.elice.showpet.domain.Category;
import com.elice.showpet.dto.AddCategoryRequest;
import com.elice.showpet.dto.CategoryResponse;
import com.elice.showpet.dto.UpdateCategoryRequest;
import com.elice.showpet.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CategoryApiController {
    private final CategoryService categoryService;

    @PostMapping("/api/category/create")
    public ResponseEntity<Category> addArticle(@RequestBody AddCategoryRequest request) {
        Category savedCategory = categoryService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedCategory);
    }

    @GetMapping("/api/category")
    public ResponseEntity<List<CategoryResponse>> findAllCategory() {
        List<CategoryResponse> category = categoryService.findAll()
                .stream()
                .map(CategoryResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(category);
    }

    @GetMapping("/api/category/{id}")
    public ResponseEntity<CategoryResponse> findCategory(@PathVariable("id") long id) {
        Category category = categoryService.findById(id);

        return ResponseEntity.ok()
                .body(new CategoryResponse(category));
    }

    @DeleteMapping("/api/category/{id}/delete")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") long id) {
        categoryService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/category/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") long id, @RequestBody UpdateCategoryRequest request) {
        Category updateCategory = categoryService.update(id, request);

        return ResponseEntity.ok()
                .body(updateCategory);
    }
}
