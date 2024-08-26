package com.elice.showpet.article.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateArticleDto {
  @NotBlank(message = "제목은 필수 입력값입니다.")
  private String title;
  private String content;
  private String image;

  @Min(value = 1, message = "카테고리 id는 필수값입니다.")
  private Long categoryId;
}