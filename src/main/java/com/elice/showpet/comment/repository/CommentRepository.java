package com.elice.showpet.comment.repository;

import com.elice.showpet.comment.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> getAllComments(Long articleId);

    Optional<Comment> getComment(Long commentId);

    Comment upsertComment(Comment comment);

    Comment upsertComment(Long articleId, Comment comment);

    void deleteComment(Comment comment);

}
