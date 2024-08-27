package com.elice.showpet.category.controller;


import com.elice.showpet.article.entity.Article;
import com.elice.showpet.article.entity.ResponseArticleDto;
import com.elice.showpet.article.service.ArticleViewService;
import com.elice.showpet.aws.s3.service.S3BucketService;
import com.elice.showpet.category.dto.AddCategoryRequest;
import com.elice.showpet.category.dto.CategoryListViewResponse;
import com.elice.showpet.category.dto.CategoryViewResponse;
import com.elice.showpet.category.dto.UpdateCategoryRequest;
import com.elice.showpet.category.entity.Category;
import com.elice.showpet.category.service.CategoryService;
import com.elice.showpet.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Controller
@RequestMapping("/category")
public class CategoryViewController {

    private final CategoryService categoryService;
    private final S3BucketService s3BucketService;
    private final ArticleViewService articleViewService;

    @GetMapping()
    public String getCategory(Model model) { // 카테고리 리스트 화면 띄우기
        try {
            List<CategoryListViewResponse> category = categoryService.findAll()
                    .stream()
                    .map(CategoryListViewResponse::new)
                    .toList();
            model.addAttribute("category", category);

            return "category/boardList";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/new") // 신규 카테고리 생성
    public String newCategory(@RequestParam(required = false, name = "id") Long id, Model model) {
        try {
            if (id == null) { // id가 없으면 새롭게 블로그를 만든다.
                model.addAttribute("category", new CategoryViewResponse());
            }
            return "category/createBoard";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/edit/{id}") // 기존 카테고리 수정
    public String editCategory(@PathVariable("id") long id, Model model) {
        try {
            Category category = categoryService.findById(id);
            model.addAttribute("category", new CategoryViewResponse(category));
            return "category/editBoard";
        } catch (Exception e) {
            return "error";
        }

    }

    @PostMapping("/new") // 신규 카테고리 내용을 받기
    public String addCategory(@ModelAttribute AddCategoryRequest request,
                              @RequestParam("file") MultipartFile file) {
        try {
            if (Objects.requireNonNull(file.getContentType()).startsWith("image")) {
                String imageUrl = s3BucketService.uploadFile(file);
                request.setImage(imageUrl);
            }
            Category savedCategory = categoryService.save(request);
             return "redirect:/category";
        } catch (IOException e) {
            return "redirect:/category/new";
        }
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") long id, UpdateCategoryRequest request, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            if (Objects.requireNonNull(file.getContentType()).startsWith("image")) {
                String imageUrl = s3BucketService.uploadFile(file);
                request.setImage(imageUrl);
            }
            Category editCategory = categoryService.update(id, request);
            redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
            return "redirect:/category";
        } catch (IOException e) {
            return "redirect:/category/new";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        try {
            categoryService.delete(id);
            return "redirect:/category";
        } catch (Exception e) {
            return "error";
        }
    }


    @GetMapping("/{id}")
    public String getCategory(@PathVariable("id") int id,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              Model model) {
        try {
            Category category = categoryService.findById(id);
            List<Article> articles = articleViewService.getPagenatedArticles(id, page, size);

            model.addAttribute("category", new CategoryViewResponse(category));
            model.addAttribute("articles", articles);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);

            return "category/board";
        } catch (Exception e) {
            return "error";
        }
    }
}
