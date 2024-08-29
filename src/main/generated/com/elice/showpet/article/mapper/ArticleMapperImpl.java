package com.elice.showpet.article.mapper;

import com.elice.showpet.article.dto.CreateArticleDto;
import com.elice.showpet.article.dto.ResponseArticleDto;
import com.elice.showpet.article.dto.UpdateArticleDto;
import com.elice.showpet.article.entity.Article;
import com.elice.showpet.article.entity.ArticleBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-28T15:49:12+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class ArticleMapperImpl implements ArticleMapper {

    @Override
    public ResponseArticleDto toResponseDto(Article article) {
        if ( article == null ) {
            return null;
        }

        ResponseArticleDto responseArticleDto = new ResponseArticleDto();

        responseArticleDto.setId( article.getId() );
        responseArticleDto.setTitle( article.getTitle() );
        responseArticleDto.setContent( article.getContent() );
        responseArticleDto.setImage( article.getImage() );
        responseArticleDto.setCreatedAt( article.getCreatedAt() );
        responseArticleDto.setUpdatedAt( article.getUpdatedAt() );

        return responseArticleDto;
    }

    @Override
    public Article toEntity(CreateArticleDto dto) {
        if ( dto == null ) {
            return null;
        }

        ArticleBuilder article = Article.builder();

        article.title( dto.getTitle() );
        article.content( dto.getContent() );
        article.image( dto.getImage() );

        return article.build();
    }

    @Override
    public Article toEntity(UpdateArticleDto dto) {
        if ( dto == null ) {
            return null;
        }

        ArticleBuilder article = Article.builder();

        article.title( dto.getTitle() );
        article.content( dto.getContent() );
        article.image( dto.getImage() );

        return article.build();
    }
}
