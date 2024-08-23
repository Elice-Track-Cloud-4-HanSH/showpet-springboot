package com.elice.showpet.comment.service;

import com.elice.showpet.comment.dto.CommentRequestDto;
import com.elice.showpet.comment.entity.Comment;
import com.elice.showpet.comment.mapper.CommentMapper;
import com.elice.showpet.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void createComment(Long articleId, CommentRequestDto commentRequestDto) {
        Comment comment = commentMapper.commentRequestDtoToComment(commentRequestDto);
        commentRepository.saveComment(articleId, comment);
    }

    // 댓글 수정
    public void updateComment(Long commentId, CommentRequestDto commentRequestDto) throws Exception {
        Comment comment = commentRepository.getComment(commentId).orElseThrow(() -> new Exception("댓글을 찾을 수 없습니다."));

        Optional.ofNullable(commentRequestDto.getContent())
                .ifPresent(comment::setContent);

        commentRepository.saveComment(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) throws Exception {
        Comment comment = commentRepository.getComment(commentId).orElseThrow(() -> new Exception("댓글을 찾을 수 없습니다."));
        commentRepository.deleteComment(comment);
    }
}
