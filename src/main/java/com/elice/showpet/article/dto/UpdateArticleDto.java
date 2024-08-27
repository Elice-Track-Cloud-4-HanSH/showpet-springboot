package com.elice.showpet.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class UpdateArticleDto {
  @NotBlank(message = "제목은 필수 입력값입니다.")
  @Length(max= 200, message = "제목의 최대 글자수는 200입니다.")
  private String title;

  @Length(max = 10000, message = "게시글의 최대 글자수는 10000입니다.")
  private String content;
  private String image;
  private String imageDeleted;
}
