package com.elice.showpet.article.controller;

import com.elice.showpet.article.entity.CreateArticleDto;
import com.elice.showpet.article.entity.ResponseArticleDto;
import com.elice.showpet.article.entity.UpdateArticleDto;
import com.elice.showpet.article.service.ArticleService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@NoArgsConstructor
@RequestMapping("/articles")
public class ArticleViewController {
  private ArticleService articleService;

  @Autowired
  public ArticleViewController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping
  public String getArticles(Model model) {
    List<ResponseArticleDto> articles = articleService.getAllArticles();

    return "article/article";
  }

  @GetMapping("/{id}")
  public String getArticle(@PathVariable Long id, Model model) {
    try {
      ResponseArticleDto article = articleService.getArticle(id);
      model.addAttribute("article", article);
      model.addAttribute("comments", new ArrayList<>());
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
    @ModelAttribute CreateArticleDto createArticleDto,
    RedirectAttributes redirectAttributes
  ) {
    ResponseArticleDto responseDto = articleService.createArticle(createArticleDto);
    redirectAttributes.addAttribute("id", responseDto.getId());
    return "redirect:/articles/{id}";
  }

  @GetMapping("/edit/{id}")
  public String editArticleForm(@PathVariable Long id, Model model) {
    try {
      ResponseArticleDto responseDto = articleService.getArticle(id);
      model.addAttribute("article", responseDto);
      return "article/editArticle";
    } catch (Exception e) {
      return "error";
    }
  }

  @PostMapping("/edit/{id}")
  public String editArticle(
    @ModelAttribute UpdateArticleDto article,
    @PathVariable Long id,
    RedirectAttributes redirectAttributes
  ) {
    try {
      ResponseArticleDto responseDto = articleService.updateArticle(id, article);

      redirectAttributes.addAttribute("id", responseDto.getId());
      redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
      return "redirect:/articles/{id}";
    } catch (Exception e) {
      return "error";
    }
  }

  @DeleteMapping("/{id}")
  public String deleteArticle(
    @PathVariable Long id,
    RedirectAttributes redirectAttributes
  ) {
    try {
      articleService.deleteArticle(id);
      return "redirect:/articles";
    } catch (Exception e) {
      return "error";
    }
  }
}
