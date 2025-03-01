package com.elice.showpet.category.dto;


import com.elice.showpet.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddCategoryRequest {
    private String title;
    private String content;
    private String image;

    public Category toEntity() {
        return Category.builder()
                .title(title)
                .content(content)
                .image(image)
                .build();
    }
}
