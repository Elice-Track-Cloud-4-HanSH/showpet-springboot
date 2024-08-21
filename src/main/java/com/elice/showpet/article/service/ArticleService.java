package com.elice.showpet.article.service;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.article.entity.CreateArticleDto;
import com.elice.showpet.article.entity.ResponseArticleDto;
import com.elice.showpet.article.entity.UpdateArticleDto;
import com.elice.showpet.article.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
  private final List<Article> articles = new ArrayList<>();
  private final ArticleMapper articleMapper;

  @Autowired
  public ArticleService(ArticleMapper articleMapper) {
    this.articleMapper = articleMapper;
    for (int i = 0; i < 4; i++) {
      Article article = Article.builder()
        .id((long) (i + 1))
        .title("qwer")
        .content("asdf")
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
      articles.add(article);
    }
  }

  public List<ResponseArticleDto> getAllArticles() {
    return articles.stream().map(articleMapper::toResponseDto).collect(Collectors.toList());
  }

  public ResponseArticleDto getArticle(Long id) throws Exception {
    Article result = articles.stream().filter(article -> article.getId().equals(id)).findFirst().orElse(null);
    if (result == null) throw new Exception("해당하는 게시글이 존재하지 않습니다.");
    return articleMapper.toResponseDto(result);
  }

  public ResponseArticleDto createArticle(CreateArticleDto articleDto) {
    Article created = articleMapper.toEntity(articleDto);
    created.setId(articles.getLast().getId()+1);
    created.setCreatedAt(LocalDateTime.now());
    created.setUpdatedAt(LocalDateTime.now());
    articles.add(created);
    return articleMapper.toResponseDto(created);
  }

  public ResponseArticleDto updateArticle(Long id, UpdateArticleDto articleDto) throws Exception {
    Article target = articles.stream().filter(article -> article.getId().equals(id)).findFirst().orElse(null);
    if (target == null) throw new Exception("해당하는 게시글이 존재하지 않습니다.");

    if (articleDto.getTitle() != null) { target.setTitle(articleDto.getTitle()); }
    if (articleDto.getContent() != null) { target.setContent(articleDto.getContent()); }
    if (articleDto.getImage() != null) { target.setImage(articleDto.getImage()); }
    return articleMapper.toResponseDto(target);
  }

  public void deleteArticle(Long id) throws Exception {
    Article target = articles.stream().filter(article -> article.getId().equals(id)).findFirst().orElse(null);
    if (target == null) throw new Exception("해당하는 게시글이 존재하지 않습니다.");
    articles.remove(target);
  }
}
