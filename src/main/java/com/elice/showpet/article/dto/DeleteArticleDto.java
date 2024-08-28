package com.elice.showpet.article.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DeleteArticleDto {
    private String password;
    private Long articleId;
}
