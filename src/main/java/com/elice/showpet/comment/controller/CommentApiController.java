package com.elice.showpet.comment.controller;

import com.elice.showpet.article.service.ArticleRestService;
import com.elice.showpet.comment.dto.CommentRequestDto;
import com.elice.showpet.comment.dto.CommentResponseDto;
import com.elice.showpet.comment.exception.CommentNotFoundException;
import com.elice.showpet.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentApiController {

    private final CommentService commentService;
    private final ArticleRestService articleService;

    @Autowired
    public CommentApiController(CommentService commentService, ArticleRestService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    // 전체 댓글 조회
    @GetMapping("/articles/{articleId}")
    public ResponseEntity<List<CommentResponseDto>> getAllComments(@PathVariable("articleId") Long articleId) {
        try {
            articleService.getArticle(articleId); // 존재하는 게시글인지 확인
            List<CommentResponseDto> comments = commentService.getAllComments(articleId);
            if (comments.isEmpty()) { // 게시글 내에 댓글이 없는 경우
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
            }
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 존재하지 않는 게시글
        }
    }

    // 댓글 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable("commentId") Long commentId) {
        CommentResponseDto comment = commentService.getComment(commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    // 댓글 생성
    @PostMapping("/{articleId}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable("articleId") Long articleId, @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto createdComment = commentService.createComment(articleId, commentRequestDto);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED); // 201 Created
    }

    // 댓글 수정
    @PatchMapping("{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto updatedComment = commentService.updateComment(commentId, commentRequestDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }

    @ExceptionHandler(CommentNotFoundException.class)
    private ResponseEntity<String> handleCommentNotFound(CommentNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }


}
