package com.elice.showpet.article.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseArticleDto {
  private Long id;
  private String title;
  private String content;
  private String image;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
