package com.elice.showpet.category.service;

import com.elice.showpet.category.domain.Category;
import com.elice.showpet.category.dto.AddCategoryRequest;
import com.elice.showpet.category.dto.UpdateCategoryRequest;
import com.elice.showpet.category.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public Category save(AddCategoryRequest request) {
        return categoryRepository.save(request.toEntity());
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    public void delete(long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public Category update(long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        category.update(request.getTitle(), request.getContent());

        return category;
    }
}
