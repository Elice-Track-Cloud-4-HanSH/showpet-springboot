package com.elice.showpet.article.service;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.article.entity.CreateArticleDto;
import com.elice.showpet.article.entity.ResponseArticleDto;
import com.elice.showpet.article.entity.UpdateArticleDto;
import com.elice.showpet.article.mapper.ArticleMapper;
import com.elice.showpet.article.repository.ArticleJdbcTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {
  private final ArticleMapper articleMapper;

  private final ArticleJdbcTemplateRepository articleRepository;

  @Autowired
  public ArticleService(
    ArticleMapper articleMapper,
    ArticleJdbcTemplateRepository articleRepository
  ) {
    this.articleMapper = articleMapper;
    this.articleRepository = articleRepository;
  }

  public List<ResponseArticleDto> getAllArticles() {
    List<Article> articles = articleRepository.findAll();
    return articles.stream().map(articleMapper::toResponseDto).collect(Collectors.toList());
  }

  public ResponseArticleDto getArticle(Long id) throws Exception {
    Article article = articleRepository.findById(id).orElseThrow(() -> new Exception("Article not found"));
    return articleMapper.toResponseDto(article);
  }

  public ResponseArticleDto createArticle(CreateArticleDto articleDto) {
    Article created = articleMapper.toEntity(articleDto);
    Article result = articleRepository.save(created);
    return articleMapper.toResponseDto(result);
  }

  public ResponseArticleDto updateArticle(Long id, UpdateArticleDto articleDto) throws Exception {
    Article findArticle = articleRepository.findById(id).orElseThrow(() -> new Exception("Article not found"));

    Optional.ofNullable(articleDto.getTitle())
      .ifPresent(findArticle::setTitle);
    Optional.ofNullable(articleDto.getContent())
      .ifPresent(findArticle::setContent);
    Optional.ofNullable(articleDto.getImage())
      .ifPresent(findArticle::setImage);

    Article updated = articleRepository.save(findArticle);
    return articleMapper.toResponseDto(updated);
  }

  public void deleteArticle(Long id) throws Exception {
    Article article = articleRepository.findById(id).orElseThrow(() -> new Exception("Article not found"));
    articleRepository.delete(article);
  }
}
