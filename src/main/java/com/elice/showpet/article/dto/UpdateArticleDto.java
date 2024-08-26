package com.elice.showpet.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateArticleDto {
  @NotBlank(message = "제목은 필수 입력값입니다.")
  private String title;
  private String content;
  private String image;
  private String imageDeleted;
}
