package com.elice.showpet.category.dto;

import com.elice.showpet.category.domain.Category;
import lombok.Getter;

@Getter
public class CategoryResponse {
    private final String title;
    private final String content;

    public CategoryResponse(Category category) {
        this.title = category.getTitle();
        this.content = category.getContent();
    }
}
