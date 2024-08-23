package com.elice.showpet.category.dto;


import com.elice.showpet.category.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CategoryViewResponse {
    private Long id;
    private String title;
    private String content;

    public CategoryViewResponse(Category category) {
        this.id = category.getId();
        this.title = category.getTitle();
        this.content = category.getContent();
    }

}
