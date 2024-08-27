package com.elice.showpet.article.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class CreateArticleDto {
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Length(max = 200, message = "제목의 최대 글자수는 200입니다.")
    private String title;

    @Length(max = 10000, message = "게시글의 최대 글자수는 10000입니다.")
    private String content;
    private String image;

    @Min(value = 1, message = "카테고리 id는 필수값입니다.")
    private Long categoryId;

    private String password;
}