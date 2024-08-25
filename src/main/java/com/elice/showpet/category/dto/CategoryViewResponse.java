package com.elice.showpet.category.dto;


import com.elice.showpet.category.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryViewResponse {
    private Long id;
    private String title;
    private String content;
    private String image;

    public CategoryViewResponse(Category category) {
        this.id = category.getId();
        this.title = category.getTitle();
        this.content = category.getContent();
        this.image = category.getImage();
    }

}
