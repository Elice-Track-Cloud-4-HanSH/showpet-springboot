package com.elice.showpet.comment.service;

import com.elice.showpet.comment.dto.CommentRequestDto;
import com.elice.showpet.comment.entity.Comment;
import com.elice.showpet.comment.exception.CommentNotFoundException;
import com.elice.showpet.comment.mapper.CommentMapper;
import com.elice.showpet.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentViewService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentViewService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    // 전체 댓글 조회
    public List<Comment> getAllComments(Long articleId) {
        return commentRepository.getAllComments(articleId);
    }

    // 댓글 생성
    @Transactional
    public void createComment(Long articleId, CommentRequestDto commentRequestDto) {
        Comment comment = commentMapper.commentRequestDtoToComment(commentRequestDto);
        commentRepository.upsertComment(articleId, comment);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.getComment(commentId).
                orElseThrow(() -> new CommentNotFoundException("not found comment: " + commentId));

        Optional.ofNullable(commentRequestDto.getContent())
                .ifPresent(comment::setContent);

        commentRepository.upsertComment(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.getComment(commentId)
                .orElseThrow(() -> new CommentNotFoundException("not found comment: " + commentId));
        commentRepository.deleteComment(comment);
    }
}
