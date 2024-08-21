package com.elice.showpet.article.controller;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.article.entity.CreateArticleDto;
import com.elice.showpet.article.entity.ResponseArticleDto;
import com.elice.showpet.article.entity.UpdateArticleDto;
import com.elice.showpet.article.mapper.ArticleMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/articles")
public class ArticleRestController {
  private ArticleMapper articleMapper;
  private List<Article> articles = new ArrayList<>();

  @Autowired
  public ArticleRestController(ArticleMapper articleMapper) {
    this.articleMapper = articleMapper;
    for (int i = 0; i < 4; i++) {
      Article article = Article.builder()
                          .id((long) (i+1))
                          .title("qwer")
                          .content("asdf")
                          .createdAt(LocalDateTime.now())
                          .updatedAt(LocalDateTime.now())
                          .build();
      articles.add(article);
    }
  }

  @GetMapping
  public ResponseEntity<List<ResponseArticleDto>> getArticles() {
    return new ResponseEntity<>(articles.stream().map(article -> articleMapper.toResponseDto(article)).collect(Collectors.toList()), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseArticleDto> getArticle(@PathVariable("id") Long id) {
    Article result = articles.stream().filter(article -> article.getId().equals(id)).findFirst().orElse(null);
    if (result == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
    ResponseArticleDto responseDto = articleMapper.toResponseDto(result);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ResponseArticleDto> createArticle(@RequestBody CreateArticleDto requestArticleDto) {
    Article created = articleMapper.toEntity(requestArticleDto);
    created.setId(articles.getLast().getId()+1);
    created.setCreatedAt(LocalDateTime.now());
    created.setUpdatedAt(LocalDateTime.now());
    articles.add(created);
    ResponseArticleDto responseDto = articleMapper.toResponseDto(created);
    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ResponseArticleDto> updateArticle(
    @PathVariable("id") Long id,
    @RequestBody UpdateArticleDto requestArticleDto
  ) {
    Article target = articles.stream().filter(article -> article.getId().equals(id)).findFirst().orElse(null);
    if (target == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }

    if (requestArticleDto.getTitle() != null) { target.setTitle(requestArticleDto.getTitle()); }
    if (requestArticleDto.getContent() != null) { target.setContent(requestArticleDto.getContent()); }
    if (requestArticleDto.getImage() != null) { target.setImage(requestArticleDto.getImage()); }
    ResponseArticleDto responseDto = articleMapper.toResponseDto(target);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseArticleDto> deleteArticle(@PathVariable("id") Long id) {
    Article target = articles.stream().filter(article -> article.getId().equals(id)).findFirst().orElse(null);
    if (target == null) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }
    articles.remove(target);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
