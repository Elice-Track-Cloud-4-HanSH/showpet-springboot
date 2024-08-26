package com.elice.showpet.comment.repository;

import com.elice.showpet.comment.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Comment> commentRowMapper = (rs, rowNum) -> {
        return Comment.builder()
                .id(rs.getLong("id"))
                .content(rs.getString("content"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .updatedAt(rs.getObject("updated_at", LocalDateTime.class))
                .build();
    };

    @Override
    public List<Comment> getAllComments(Long articleId) {
        String sql = "SELECT * FROM comment WHERE article_id = ?";
        return jdbcTemplate.query(sql, commentRowMapper, articleId);
    }

    @Override
    public Optional<Comment> getComment(Long commentId) {
        String sql = "SELECT * FROM comment WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{commentId}, commentRowMapper).stream().findFirst();
    }

    @Override
    public Comment upsertComment(Comment comment) {
        if (comment.getId() == null) {
            String insertSql = "INSERT INTO comment (content, created_at, updated_at) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"id"});
                        ps.setString(1, comment.getContent());
                        ps.setObject(2, LocalDateTime.now());
                        ps.setObject(3, LocalDateTime.now());
                        return ps;
                    }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                comment.setId(key.longValue());
            }
        } else {
            String updateSql = "UPDATE comment SET content = ?, updated_at = ? WHERE id = ?";
            jdbcTemplate.update(updateSql, comment.getContent(), LocalDateTime.now(), comment.getId());
        }
        return comment;
    }

    @Override
    public Comment upsertComment(Long articleId, Comment comment) {
        if (comment.getId() == null) {
            String insertSql = "INSERT INTO comment (content, created_at, updated_at, article_id) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"id"});
                        ps.setString(1, comment.getContent());
                        ps.setObject(2, LocalDateTime.now());
                        ps.setObject(3, LocalDateTime.now());
                        ps.setLong(4, articleId);
                        return ps;
                    }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                comment.setId(key.longValue());
            }
        } else {
            String updateSql = "UPDATE comment SET content = ?, updated_at = ? WHERE id = ?";
            jdbcTemplate.update(updateSql, comment.getContent(), LocalDateTime.now(), comment.getId());
        }
        return comment;
    }

    @Override
    public void deleteComment(Comment comment) {
        String sql = "DELETE FROM comment WHERE id = ?";
        jdbcTemplate.update(sql, comment.getId());
    }

}
