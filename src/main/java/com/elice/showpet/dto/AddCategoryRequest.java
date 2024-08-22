package com.elice.showpet.dto;


import com.elice.showpet.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddCategoryRequest {
    private String title;
    private String content;

    public Category toEntity() {
        return Category.builder()
                .title(title)
                .content(content)
                .build();
    }
}
