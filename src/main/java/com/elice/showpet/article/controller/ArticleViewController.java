package com.elice.showpet.article.controller;

import com.elice.showpet.article.entity.*;
import com.elice.showpet.article.service.ArticleViewService;
import com.elice.showpet.aws.s3.service.S3BucketService;
import com.elice.showpet.comment.entity.Comment;
import com.elice.showpet.comment.service.CommentViewService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@NoArgsConstructor
@RequestMapping("/articles")
public class ArticleViewController {
  private ArticleViewService articleViewService;
  private S3BucketService s3BucketService;
  private CommentViewService commentViewService;

  @Autowired
  public ArticleViewController(
    ArticleViewService articleViewService,
    CommentViewService commentViewService,
    S3BucketService s3Service
  ) {
    this.articleViewService = articleViewService;
    this.commentViewService = commentViewService;
    this.s3BucketService = s3Service;
  }

  @GetMapping
  public String getArticles(Model model) {
    List<Article> articles = articleViewService.getAllArticles();

    return "article/article";
  }

  @GetMapping("/{id}")
  public String getArticle(
    @PathVariable("id") Long id,
    Model model
  ) {
    try {
      Article article = articleViewService.getArticle(id);
      model.addAttribute("article", article);

      // 댓글 리스트 조회
      List<Comment> comments = commentViewService.getAllComments(id);
      article.setComments(comments);
      model.addAttribute("comments", comments);

      return "article/article";
    } catch (Exception e) {
      return "error";
    }
  }

  @GetMapping("/add")
  public String addArticleForm(Model model) {
    return "article/createArticle";
  }

  @PostMapping("/add")
  public String addArticle(
    @ModelAttribute CreateArticleDto articleDto,
    @RequestParam("file") MultipartFile file,
    RedirectAttributes redirectAttributes
  ) {
    try {
      if (Objects.requireNonNull(file.getContentType()).startsWith("image")) {
        String imageUrl = s3BucketService.uploadFile(file);
        articleDto.setImage(imageUrl);
      }
      Article article = articleViewService.createArticle(articleDto);
      redirectAttributes.addAttribute("id", article.getId());
      return "redirect:/articles/{id}";
    } catch (IOException e) {
      return "redirect:/articles/add";
    }
  }

  @GetMapping("/edit/{id}")
  public String editArticleForm(
    @PathVariable("id") Long id,
    Model model
  ) {
    try {
      Article article = articleViewService.getArticle(id);
      article.setImage(article.getImage());
      model.addAttribute("article", article);
      return "article/editArticle";
    } catch (Exception e) {
      return "error";
    }
  }

  @PostMapping("/edit/{id}")
  public String editArticle(
    @ModelAttribute UpdateArticleDto articleDto,
    @RequestParam("file") MultipartFile file,
    @PathVariable("id") Long id,
    RedirectAttributes redirectAttributes
  ) {
    try {
      if (Objects.requireNonNull(file.getContentType()).startsWith("image")) {
        String imageUrl = s3BucketService.uploadFile(file);
        articleDto.setImage(imageUrl);
      }
      Article article = articleViewService.updateArticle(id, articleDto);

      redirectAttributes.addAttribute("id", article.getId());
      redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
      return "redirect:/articles/{id}";
    } catch (Exception e) {
      return "redirect:/articles/edit/{id}";
    }
  }

  @DeleteMapping("/{id}")
  public String deleteArticle(
    @PathVariable("id") Long id,
    RedirectAttributes redirectAttributes
  ) {
    try {
      articleViewService.deleteArticle(id);
      return "redirect:/boards";
    } catch (Exception e) {
      return "error";
    }
  }
}
