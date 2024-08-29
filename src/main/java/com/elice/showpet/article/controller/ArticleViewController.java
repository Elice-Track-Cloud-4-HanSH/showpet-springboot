package com.elice.showpet.article.controller;

import com.elice.showpet.article.dto.CreateArticleDto;
import com.elice.showpet.article.dto.UpdateArticleDto;
import com.elice.showpet.article.entity.*;
import com.elice.showpet.article.service.ArticleViewService;
import com.elice.showpet.aws.s3.service.S3BucketService;
import com.elice.showpet.comment.dto.CommentResponseDto;
import com.elice.showpet.comment.entity.Comment;
import com.elice.showpet.comment.service.CommentViewService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@NoArgsConstructor
@RequestMapping("/articles")
public class ArticleViewController {
    private ArticleViewService articleViewService;
    private S3BucketService s3BucketService;
    private CommentViewService commentViewService;

    @Value("${spring.enabled.anon}")
    private boolean isEnabledAnon;

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


    @GetMapping("/{id}")
    public String getArticle(
            @PathVariable("id") Long id,
            Model model
    ) {
        if (id < 1) {
            return "redirect:/category";
        }
        try {
            Article article = articleViewService.getArticle(id);
            model.addAttribute("article", article);
            model.addAttribute("categoryId", article.getCategory().getId());

            // 댓글 리스트 조회
            List<CommentResponseDto> comments = commentViewService.getAllComments(id);
            model.addAttribute("comments", comments);

            return isEnabledAnon ? "article/articleAnon" : "article/article";
        } catch (Exception e) {
            return "redirect:/category";
        }
    }

    @GetMapping("/add")
    public String addArticleForm(
            @RequestParam(value = "category-id", required = false) Long categoryId,
            Model model
    ) {
        categoryId = Objects.isNull(categoryId) ? 1 : categoryId;
        model.addAttribute("categoryId", categoryId);
        return isEnabledAnon ? "article/createArticleAnon" : "article/createArticle";
    }

    @PostMapping("/add")
    public String addArticle(
            @Validated @ModelAttribute CreateArticleDto articleDto,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes,
            Errors errors
    ) {
        try {
            if (Objects.requireNonNull(file.getContentType()).startsWith("image")) {
                String imageUrl = s3BucketService.uploadFile(file, "article");
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
        if (id < 1) {
            return "redirect:/category";
        }
        try {
            Article article = articleViewService.getArticle(id);
            model.addAttribute("article", article);
            return isEnabledAnon ? "article/editArticleAnon" : "article/editArticle";
        } catch (Exception e) {
            return "redirect:/category";
        }
    }

    @PostMapping("/edit/{id}")
    public String editArticle(
            @Validated @ModelAttribute UpdateArticleDto articleDto,
            @RequestParam("file") MultipartFile file,
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes,
            Errors error
    ) {
        redirectAttributes.addAttribute("id", id);
        if (error.hasErrors()) {
            return "redirect:/articles/edit/{id}";
        }
        if (!articleViewService.verifyPassword(id, articleDto.getPassword()) && isEnabledAnon) {
            return "redirect:/articles/{id}";
        }
        try {
            if (Objects.requireNonNull(file.getContentType()).startsWith("image")) {
                String imageUrl = s3BucketService.uploadFile(file);
                articleDto.setImage(imageUrl);
            }
            Article article = articleViewService.updateArticle(id, articleDto);

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
        if (id < 1) {
            return "redirect:/category";
        }
        try {
            Long categoryId = articleViewService.deleteArticle(id);
            redirectAttributes.addAttribute("categoryId", categoryId);
            return "redirect:/category/{categoryId}";
        } catch (Exception e) {
            redirectAttributes.addAttribute("id", id);
            return "redirect:/articles/{id}";
        }
    }
}
