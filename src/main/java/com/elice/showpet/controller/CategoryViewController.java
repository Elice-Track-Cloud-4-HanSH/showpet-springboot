package com.elice.showpet.controller;


import com.elice.showpet.domain.Category;
import com.elice.showpet.dto.AddCategoryRequest;
import com.elice.showpet.dto.CategoryListViewResponse;
import com.elice.showpet.dto.CategoryViewResponse;
import com.elice.showpet.dto.UpdateCategoryRequest;
import com.elice.showpet.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@Controller
@RequestMapping("/category")
public class CategoryViewController {

    private final CategoryService categoryService;

    @GetMapping()
    public String getArticles(Model model) {
        List<CategoryListViewResponse> category = categoryService.findAll()
                .stream()
                .map(CategoryListViewResponse::new)
                .toList();
        model.addAttribute("category", category);

        return "board/boardList";
    }

//    @GetMapping("/category/{id}")
//    public String getArticle(@PathVariable("id") long id, Model model) {
//        Category category = categoryService.findById(id);
//        model.addAttribute("article", new CategoryViewResponse(category));
//        return "board/board";
//    }

    @GetMapping("/new")
    public String newCategory(@RequestParam(required = false, name = "id") Long id, Model model) {
        if (id == null) { // id가 없으면 새롭게 블로그를 만든다.
            model.addAttribute("category", new CategoryViewResponse());

//        } else {
//            Category category = categoryService.findById(id);
//            model.addAttribute("category", new CategoryViewResponse(category));
//            return "board/editBoard";
//        }
        }
        return "board/createBoard";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") long id, Model model) {
            Category category = categoryService.findById(id);
            model.addAttribute("category", new CategoryViewResponse(category));
            return "board/editBoard";
    }

    @PostMapping("/new")
    public String addCategory(@ModelAttribute AddCategoryRequest request) {
        Category savedCategory = categoryService.save(request);
        return "redirect:/category";
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") long id, UpdateCategoryRequest request) {
        Category savedCategory = categoryService.update(id, request);
        return "redirect:/category";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return "redirect:/category";
    }
}
