package com.elice.showpet.comment.controller;

import com.elice.showpet.comment.dto.CommentRequestDto;
import com.elice.showpet.comment.dto.CommentResponseDto;
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

    @Autowired
    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 전체 댓글 조회
    @GetMapping("/articles/{articleId}")
    public ResponseEntity<List<CommentResponseDto>> getAllComments(@PathVariable("articleId") Long articleId) {
        List<CommentResponseDto> comments = commentService.getAllComments(articleId);
        if(comments.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // 댓글 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable("commentId") Long commentId) {
        try {
            CommentResponseDto comment = commentService.getComment(commentId);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
        try {
            CommentResponseDto updatedComment = commentService.updateComment(commentId, commentRequestDto);
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
