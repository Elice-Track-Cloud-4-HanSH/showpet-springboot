package com.elice.showpet.article.controller;

import com.elice.showpet.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
//@NoArgsConstructor
@RequestMapping("/api/articles")
public class ArticleRestController {
  List<Article> articles = new ArrayList<>();

  public ArticleRestController() {
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
  public ResponseEntity<List<Article>> getArticles() {
    return new ResponseEntity<>(articles, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Article> getArticle(@PathVariable("id") Long id) {
    Article result = articles.stream().filter(article -> article.getId().equals(id)).findFirst().orElse(null);
    if (result == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Article> createArticle(@RequestBody Article requestArticleDto) {
    Article created = Article.builder()
                      .id(articles.getLast().getId() + 1)
                      .title(requestArticleDto.getTitle())
                      .content(requestArticleDto.getContent())
                      .image(requestArticleDto.getImage())
                      .createdAt(LocalDateTime.now())
                      .updatedAt(LocalDateTime.now())
                      .build();
    articles.add(created);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Article> updateArticle(
    @PathVariable("id") Long id,
    @RequestBody Article requestArticleDto
  ) {
    Article target = articles.stream().filter(article -> article.getId().equals(id)).findFirst().orElse(null);
    System.out.println(requestArticleDto);
    if (target == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
    if (requestArticleDto.getTitle() != null) { target.setTitle(requestArticleDto.getTitle()); }
    if (requestArticleDto.getContent() != null) { target.setContent(requestArticleDto.getContent()); }
    if (requestArticleDto.getImage() != null) { target.setImage(requestArticleDto.getImage()); }
    return new ResponseEntity<>(target, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Article> deleteArticle(@PathVariable("id") Long id) {
    Article target = articles.stream().filter(article -> article.getId().equals(id)).findFirst().orElse(null);
    if (target == null) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }
    articles.remove(target);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
