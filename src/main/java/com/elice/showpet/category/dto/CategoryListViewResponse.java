package com.elice.showpet.category.dto;

import com.elice.showpet.category.entity.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryListViewResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String image;

    public CategoryListViewResponse(Category category) {
        this.id = category.getId();
        this.title = category.getTitle();
        this.content = category.getContent();
        this.image = category.getImage();
    }
}
