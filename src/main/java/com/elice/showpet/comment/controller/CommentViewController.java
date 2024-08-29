package com.elice.showpet.comment.controller;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.article.service.ArticleViewService;
import com.elice.showpet.comment.dto.CommentRequestDto;
import com.elice.showpet.comment.dto.CommentResponseDto;
import com.elice.showpet.comment.exception.CommentNotFoundException;
import com.elice.showpet.comment.service.CommentViewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentViewController {

    private final CommentViewService commentViewService;
    private final ArticleViewService articleViewService;

    // 댓글 생성
    // http://localhost:8080/comments?articleId=1
    @PostMapping
    public String createComment(
            @RequestParam("articleId") Long articleId,
            @Valid @ModelAttribute CommentRequestDto commentRequestDto,
            BindingResult bindingResult,
            Model model) {

        if(bindingResult.hasErrors()){
            return handleCommentError(articleId, bindingResult, model, "errorMessage");
        }

        commentViewService.createComment(articleId, commentRequestDto);
        return "redirect:/articles/" + articleId;

    }

    // 댓글 수정
    // http://localhost:8080/comments/edit/3?arcicleId=1
    @PostMapping("/edit/{commentId}")
    public String updateComment(
            @PathVariable("commentId") Long commentId,
            @RequestParam("articleId") Long articleId,
            @Valid @ModelAttribute CommentRequestDto commentRequestDto,
            BindingResult bindingResult,
            Model model) {

        if(bindingResult.hasErrors()){
            model.addAttribute("commentId", commentId);
            model.addAttribute("commentContent", commentRequestDto.getContent());
            return handleCommentError(articleId, bindingResult, model, "updateErrorMessage");
        }

        // 비밀번호 확인
        CommentResponseDto comment = commentViewService.getComment(commentId);
        if(!comment.getPassword().equals(commentRequestDto.getPassword())){
            model.addAttribute("diffPasswordMessage", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("commentId", commentId);
            model.addAttribute("commentContent", commentRequestDto.getContent());

            bringArticleData(articleId, model); // 게시글, 댓글 정보

            return "article/article";
        }

        commentViewService.updateComment(commentId, commentRequestDto);
        return "redirect:/articles/" + articleId;

    }

    // 댓글 삭제
    // http://localhost:8080/comments/3
    @DeleteMapping("/{commentId}")
    @ResponseBody
    public ResponseEntity deleteComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody Map<String, String> requestBody) {

        String password = requestBody.get("password");

        try {
            CommentResponseDto comment = commentViewService.getComment(commentId);
            if(!comment.getPassword().equals(password)){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
            }
            commentViewService.deleteComment(commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (CommentNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private String handleCommentError(Long articleId, BindingResult bindingResult, Model model, String errorAttribute){
        String errorMessage = bindingResult.getFieldError().getDefaultMessage();
        model.addAttribute(errorAttribute, errorMessage);
        bringArticleData(articleId, model);
        return "article/article";
    }

    private void bringArticleData(Long articleId, Model model){
        Article article = articleViewService.getArticle(articleId);
        model.addAttribute("article", article);

        List<CommentResponseDto> comments = commentViewService.getAllComments(articleId);
        model.addAttribute("comments", comments);
    }

    @ExceptionHandler({CommentNotFoundException.class, IllegalArgumentException.class})
    private String handleException() {
        return "error";
    }

}
