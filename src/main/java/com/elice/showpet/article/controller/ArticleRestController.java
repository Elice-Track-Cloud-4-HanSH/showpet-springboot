package com.elice.showpet.article.controller;

import com.elice.showpet.article.dto.CreateArticleDto;
import com.elice.showpet.article.dto.DeleteArticleDto;
import com.elice.showpet.article.dto.ResponseArticleDto;
import com.elice.showpet.article.dto.UpdateArticleDto;
import com.elice.showpet.article.entity.Article;
import com.elice.showpet.article.mapper.ArticleMapper;
import com.elice.showpet.article.service.ArticleRestService;
import com.elice.showpet.article.service.ArticleViewService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@NoArgsConstructor
@RequestMapping("/api/articles")
public class ArticleRestController {
    private ArticleRestService articleRestService;
    private ArticleViewService articleViewService;
    private ArticleMapper mapper;

    @Autowired
    public ArticleRestController(ArticleRestService articleRestService, ArticleViewService articleViewService, ArticleMapper mapper) {
        this.articleRestService = articleRestService;
        this.articleViewService = articleViewService;
        this.mapper = mapper;
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

    @PostMapping("/validate-password")
    public ResponseEntity<?> validatePassword(@RequestBody DeleteArticleDto deleteArticleDto) {
        System.out.println(deleteArticleDto);
        boolean valid = articleViewService.verifyPassword(deleteArticleDto.getArticleId(), deleteArticleDto.getPassword());
        if (valid) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/all/{id}")
    public ResponseEntity<?> deleteAllArticlesRelatedWithCategory(@PathVariable("id") Integer id) {
        articleViewService.deleteAllArticlesRelatedWithCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResponseArticleDto>> searchArticles(@RequestParam("key") String key, @RequestParam("category-id") Integer categoryId) {
        List<ResponseArticleDto> responseArticleDto = articleViewService.searchArticle(categoryId, key).stream().map(mapper::toResponseDto).toList();
        return new ResponseEntity<>(responseArticleDto, HttpStatus.OK);
    }
}
