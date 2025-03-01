package com.elice.showpet.comment.service;

import com.elice.showpet.comment.dto.CommentRequestDto;
import com.elice.showpet.comment.dto.CommentResponseDto;
import com.elice.showpet.comment.entity.Comment;
import com.elice.showpet.comment.exception.CommentNotFoundException;
import com.elice.showpet.comment.mapper.CommentMapper;
import com.elice.showpet.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    // 전체 댓글 조회
    public List<CommentResponseDto> getAllComments(Long articleId) {
        List<Comment> comments = commentRepository.getAllComments(articleId);
        return comments.stream()
                .map(commentMapper::commentToCommentResponseDto)
                .collect(Collectors.toList());
    }

    // 댓글 조회
    public CommentResponseDto getComment(Long commentId) {
        Comment comment = commentRepository.getComment(commentId)
                .orElseThrow(() -> new CommentNotFoundException("not found comment: " + commentId));
        return commentMapper.commentToCommentResponseDto(comment);
    }

    // 댓글 생성
    @Transactional
    public CommentResponseDto createComment(Long articleId, CommentRequestDto commentRequestDto) {
        Comment comment = commentMapper.commentRequestDtoToComment(commentRequestDto);
        Comment createdComment = commentRepository.upsertComment(articleId, comment);
        return commentMapper.commentToCommentResponseDto(createdComment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.getComment(commentId)
                .orElseThrow(() -> new CommentNotFoundException("not found comment: " + commentId));

        Optional.ofNullable(commentRequestDto.getContent())
                .ifPresent(comment::setContent);

        Comment updatedComment = commentRepository.upsertComment(comment);
        return commentMapper.commentToCommentResponseDto(updatedComment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.getComment(commentId)
                .orElseThrow(() -> new CommentNotFoundException("not found comment: " + commentId));
        commentRepository.deleteComment(comment);
    }

}
