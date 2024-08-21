package com.elice.showpet.article.controller;

import com.elice.showpet.article.entity.CreateArticleDto;
import com.elice.showpet.article.entity.ResponseArticleDto;
import com.elice.showpet.article.entity.UpdateArticleDto;
import com.elice.showpet.article.service.ArticleRestService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@NoArgsConstructor
@RequestMapping("/api/articles")
public class ArticleRestController {
  private ArticleRestService articleRestService;

  @Autowired
  public ArticleRestController(ArticleRestService articleRestService) {
    this.articleRestService = articleRestService;
  }

  @GetMapping
  public ResponseEntity<List<ResponseArticleDto>> getArticles() {
    List<ResponseArticleDto> result = articleRestService.getAllArticles();
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseArticleDto> getArticle(@PathVariable("id") Long id) {
    try {
      ResponseArticleDto responseDto = articleRestService.getArticle(id);
      return new ResponseEntity<>(responseDto, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping
  public ResponseEntity<ResponseArticleDto> createArticle(@RequestBody CreateArticleDto requestArticleDto) {
    ResponseArticleDto responseDto = articleRestService.createArticle(requestArticleDto);
    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ResponseArticleDto> updateArticle(
    @PathVariable("id") Long id,
    @RequestBody UpdateArticleDto updateArticleDto
  ) {
    try {
      ResponseArticleDto responseDto = articleRestService.updateArticle(id, updateArticleDto);
      return new ResponseEntity<>(responseDto, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseArticleDto> deleteArticle(@PathVariable("id") Long id) {
    try {
      articleRestService.deleteArticle(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}
