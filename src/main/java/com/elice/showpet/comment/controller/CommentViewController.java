package com.elice.showpet.comment.controller;

import com.elice.showpet.comment.dto.CommentRequestDto;
import com.elice.showpet.comment.exception.CommentNotFoundException;
import com.elice.showpet.comment.service.CommentViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentViewController {

    private final CommentViewService commentViewService;

    @Autowired
    public CommentViewController(CommentViewService commentViewService) {
        this.commentViewService = commentViewService;
    }

    // 댓글 생성
    // http://localhost:8080/comments?articleId=1
    @PostMapping("/comments")
    public String getAllComments(@RequestParam("articleId") Long articleId, @ModelAttribute CommentRequestDto commentRequestDto) {
        commentViewService.createComment(articleId, commentRequestDto);
        return "redirect:/articles/" + articleId;

    }

    // 댓글 수정
    // http://localhost:8080/comments/edit/3?arcicleId=1
    @PostMapping("/comments/edit/{commentId}")
    public String updateComment(
            @PathVariable("commentId") Long commentId,
            @RequestParam("articleId") Long articleId,
            @ModelAttribute CommentRequestDto commentRequestDto) {

        commentViewService.updateComment(commentId, commentRequestDto);
        return "redirect:/articles/" + articleId;

    }

    // 댓글 삭제
    // http://localhost:8080/comments/3
    @DeleteMapping("/comments/{commentId}")
    @ResponseBody
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId) {
        try {
            commentViewService.deleteComment(commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (CommentNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(CommentNotFoundException.class)
    private String handleCommentNotFoundException() {
        return "error";
    }

}
