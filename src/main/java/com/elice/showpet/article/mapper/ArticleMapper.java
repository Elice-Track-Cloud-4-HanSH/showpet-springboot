package com.elice.showpet.article.mapper;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.article.entity.CreateArticleDto;
import com.elice.showpet.article.entity.ResponseArticleDto;
import com.elice.showpet.article.entity.UpdateArticleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
  ResponseArticleDto toResponseDto(Article article);
  Article toEntity(CreateArticleDto dto);
  Article toEntity(UpdateArticleDto dto);
}
