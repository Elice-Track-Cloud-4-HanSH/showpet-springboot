package com.elice.showpet.article.controller;

import com.elice.showpet.article.entity.CreateArticleDto;
import com.elice.showpet.article.entity.ResponseArticleDto;
import com.elice.showpet.article.entity.ResponseArticleWithBase64ImageDto;
import com.elice.showpet.article.entity.UpdateArticleDto;
import com.elice.showpet.article.service.ArticleService;
import com.elice.showpet.utils.Base64Codec;
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
      ResponseArticleWithBase64ImageDto dto = new ResponseArticleWithBase64ImageDto(article);
      model.addAttribute("article", dto);
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
    @ModelAttribute CreateArticleDto articleDto,
    @RequestParam("file") MultipartFile file,
    RedirectAttributes redirectAttributes
  ) throws IOException {
    if (!file.getContentType().startsWith("application")) {
      String base64EncodedFile = Base64Codec.encode(file);
      String mimeType = file.getContentType();
      articleDto.setImage(mimeType + "," + base64EncodedFile);
    }
    ResponseArticleDto responseDto = articleService.createArticle(articleDto);
    redirectAttributes.addAttribute("id", responseDto.getId());
    return "redirect:/articles/{id}";
  }

  @GetMapping("/edit/{id}")
  public String editArticleForm(@PathVariable Long id, Model model) {
    try {
      ResponseArticleDto responseDto = articleService.getArticle(id);
      ResponseArticleWithBase64ImageDto dto = new ResponseArticleWithBase64ImageDto(responseDto);
      model.addAttribute("article", dto);
      return "article/editArticle";
    } catch (Exception e) {
      return "error";
    }
  }

  @PostMapping("/edit/{id}")
  public String editArticle(
    @ModelAttribute UpdateArticleDto articleDto,
    @RequestParam("file") MultipartFile file,
    @PathVariable Long id,
    RedirectAttributes redirectAttributes
  ) {
    try {
      if (!file.getContentType().startsWith("application")) {
        String base64EncodedFile = Base64Codec.encode(file);
        String mimeType = file.getContentType();
        articleDto.setImage(mimeType + "," + base64EncodedFile);
      }
      ResponseArticleDto responseDto = articleService.updateArticle(id, articleDto);

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
