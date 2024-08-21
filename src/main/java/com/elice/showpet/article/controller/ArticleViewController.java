package com.elice.showpet.article.controller;

import com.elice.showpet.article.entity.*;
import com.elice.showpet.article.service.ArticleViewService;
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
  private ArticleViewService articleViewService;

  @Autowired
  public ArticleViewController(ArticleViewService articleViewService) {
    this.articleViewService = articleViewService;
  }

  @GetMapping
  public String getArticles(Model model) {
    List<Article> articles = articleViewService.getAllArticles();

    return "article/article";
  }

  @GetMapping("/{id}")
  public String getArticle(@PathVariable Long id, Model model) {
    try {
      Article article = articleViewService.getArticle(id);
      article.setImage(parseImage(article.getImage()));
      model.addAttribute("article", article);
      model.addAttribute("comments", new ArrayList<String>());
      return "article/article";
    } catch (Exception e) {
      return "error";
    }
  }

  private String parseImage(String image) {
    try {
      if (image == null || image.isEmpty()) throw new Exception();
      String[] imageInfo = image.split(","); // image[0] : mimeType, image[1] : base64 data
      image = "data:" + imageInfo[0] + ";base64," + imageInfo[1];
    } catch (Exception e) {
      image = null;
    }
    return image;
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
    Article article = articleViewService.createArticle(articleDto);
    redirectAttributes.addAttribute("id", article.getId());
    return "redirect:/articles/{id}";
  }

  @GetMapping("/edit/{id}")
  public String editArticleForm(@PathVariable Long id, Model model) {
    try {
      Article article = articleViewService.getArticle(id);
      article.setImage(parseImage(article.getImage()));
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
    @PathVariable Long id,
    RedirectAttributes redirectAttributes
  ) {
    try {
      if (!file.getContentType().startsWith("application")) {
        String base64EncodedFile = Base64Codec.encode(file);
        String mimeType = file.getContentType();
        articleDto.setImage(mimeType + "," + base64EncodedFile);
      }
      Article article = articleViewService.updateArticle(id, articleDto);

      redirectAttributes.addAttribute("id", article.getId());
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
      articleViewService.deleteArticle(id);
      return "redirect:/articles";
    } catch (Exception e) {
      return "error";
    }
  }
}
