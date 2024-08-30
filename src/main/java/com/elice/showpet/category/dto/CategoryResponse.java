package com.elice.showpet.category.dto;

import com.elice.showpet.category.entity.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
    private final String title;
    private final String content;
    private String keyword; // 검색 키워드
    private String type; // 검색 타입

    public CategoryResponse(Category category) {
        this.title = category.getTitle();
        this.content = category.getContent();
    }
}
