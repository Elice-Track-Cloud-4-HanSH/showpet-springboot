package com.elice.showpet.category.controller;


import com.elice.showpet.article.entity.Article;
import com.elice.showpet.aws.s3.service.S3BucketService;
import com.elice.showpet.category.entity.Category;
import com.elice.showpet.category.dto.AddCategoryRequest;
import com.elice.showpet.category.dto.CategoryListViewResponse;
import com.elice.showpet.category.dto.CategoryViewResponse;
import com.elice.showpet.category.dto.UpdateCategoryRequest;
import com.elice.showpet.category.service.CategoryService;
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

    @GetMapping()
    public String getCategory(Model model) { // 카테고리 리스트 화면 띄우기
        List<CategoryListViewResponse> category = categoryService.findAll()
                .stream()
                .map(CategoryListViewResponse::new)
                .toList();
        model.addAttribute("category", category);

        return "category/boardList";
    }

    @GetMapping("/new") // 신규 카테고리 생성
    public String newCategory(@RequestParam(required = false, name = "id") Long id, Model model) {
        if (id == null) { // id가 없으면 새롭게 블로그를 만든다.
            model.addAttribute("category", new CategoryViewResponse());

//        } else {
//            Category category = categoryService.findById(id);
//            model.addAttribute("category", new CategoryViewResponse(category));
//            return "board/editBoard";
//        }
        }
        return "category/createBoard";
    }

    @GetMapping("/edit/{id}") // 기존 카테고리 수정
    public String editCategory(@PathVariable("id") long id, Model model) {
            Category category = categoryService.findById(id);
            model.addAttribute("category", new CategoryViewResponse(category));
            return "category/editBoard";
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
        categoryService.delete(id);
        return "redirect:/category";
    }

    //    @GetMapping("/category/{id}")
//    public String getArticle(@PathVariable("id") long id, Model model) {
//        Category category = categoryService.findById(id);
//        model.addAttribute("article", new CategoryViewResponse(category));
//        return "board/board";
//    }
}
