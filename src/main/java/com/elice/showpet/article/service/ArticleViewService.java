package com.elice.showpet.article.service;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.article.entity.CreateArticleDto;
import com.elice.showpet.article.entity.UpdateArticleDto;
import com.elice.showpet.article.mapper.ArticleMapper;
import com.elice.showpet.article.repository.ArticleJdbcTemplateRepository;
import com.elice.showpet.aws.s3.service.S3BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleViewService {
  private final ArticleMapper articleMapper;

  private final ArticleJdbcTemplateRepository articleRepository;

  private final S3BucketService s3BucketService;

  @Autowired
  public ArticleViewService(
    ArticleMapper articleMapper,
    ArticleJdbcTemplateRepository articleRepository,
    S3BucketService s3BucketService
  ) {
    this.articleMapper = articleMapper;
    this.articleRepository = articleRepository;
    this.s3BucketService = s3BucketService;
  }

  public List<Article> getAllArticles() {
    return articleRepository.findAll();
  }

  public Article getArticle(Long id) throws Exception {
    return articleRepository.findById(id).orElseThrow(() -> new Exception("Article not found"));
  }

  public Article createArticle(CreateArticleDto articleDto) {
    Article created = articleMapper.toEntity(articleDto);
    return articleRepository.save(created);
  }

  public Article updateArticle(Long id, UpdateArticleDto articleDto) throws Exception {
    Article findArticle = getArticle(id);

    Optional.ofNullable(articleDto.getImageDeleted()).flatMap(_ -> Optional.ofNullable(findArticle.getImage())).ifPresent(this::removeImage);
    Optional.ofNullable(articleDto.getTitle())
      .ifPresent(findArticle::setTitle);
    Optional.ofNullable(articleDto.getContent())
      .ifPresent(findArticle::setContent);
    Optional.ofNullable(articleDto.getImage())
      .ifPresent((image) -> {
        Optional.ofNullable(findArticle.getImage()).ifPresent(this::removeImage);
        findArticle.setImage(image);
      });

    return articleRepository.save(findArticle);
  }

  public void deleteArticle(Long id) throws Exception {
    Article article = getArticle(id);
    articleRepository.delete(article);
    removeImage(article.getImage());
  }

  public void removeImage(String image) {
    s3BucketService.deleteFile(image);
  }
}
