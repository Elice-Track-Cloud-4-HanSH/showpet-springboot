package com.elice.showpet.dto;

import com.elice.showpet.domain.Category;
import lombok.Getter;

@Getter
public class CategoryListViewResponse {
    private final Long id;
    private final String title;
    private final String content;

    public CategoryListViewResponse(Category category) {
        this.id = category.getId();
        this.title = category.getTitle();
        this.content = category.getContent();
    }
}
