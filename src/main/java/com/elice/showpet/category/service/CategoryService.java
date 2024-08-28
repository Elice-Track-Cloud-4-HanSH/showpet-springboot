package com.elice.showpet.category.service;

import com.elice.showpet.aws.s3.service.S3BucketService;
import com.elice.showpet.category.entity.Category;
import com.elice.showpet.category.dto.AddCategoryRequest;
import com.elice.showpet.category.dto.UpdateCategoryRequest;
import com.elice.showpet.category.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final S3BucketService s3BucketService;


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
//        try {
//
//        } catch (Exception e) {
//            return "error";
//        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        Optional.ofNullable(request.getTitle())
                .ifPresent(category::setTitle);
        Optional.ofNullable(request.getContent())
                .ifPresent(category::setContent);
        Optional.ofNullable(request.getImage())
                .ifPresent((image) -> {
                    Optional.ofNullable(category.getImage()).ifPresent(this::removeImage);
                    category.setImage(image);
                });
        category.update(request.getTitle(), request.getContent());

        return category;
    }
    public void removeImage(String image) {
        s3BucketService.deleteFile(image);
    }
}

