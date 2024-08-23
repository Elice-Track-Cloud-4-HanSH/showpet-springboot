package com.elice.showpet.controller;


import com.elice.showpet.domain.Category;
import com.elice.showpet.dto.CategoryListViewResponse;
import com.elice.showpet.dto.CategoryViewResponse;
import com.elice.showpet.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@RequiredArgsConstructor
@Controller
public class CategoryViewController {

    private final CategoryService categoryService;

    @GetMapping("/category")
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

    @GetMapping("/category/new")
    public String newCategory(@RequestParam(required = false, name = "id") Long id, Model model) {
        if(id == null) { // id가 없으면 새롭게 블로그를 만든다.
            model.addAttribute("category", new CategoryViewResponse());
        }
        return "board/createBoard";
    }

    @GetMapping("/category/edit/{id}")
    public String editCategory(@RequestParam(required = false, name = "id") Long id, Model model) {
        if (id != null) {
            Category category = categoryService.findById(id);
            model.addAttribute("category", new CategoryViewResponse(category));
        }
        return "board/editBoard";
    }
}
