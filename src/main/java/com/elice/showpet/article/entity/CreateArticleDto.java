package com.elice.showpet.article.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateArticleDto {
  private String title;
  private String content;
  private String image;
  private Long categoryId;
}