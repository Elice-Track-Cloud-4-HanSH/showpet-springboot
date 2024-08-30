package com.elice.showpet.comment.repository;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.article.repository.ArticleJdbcTemplateRepository;
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
    private ArticleJdbcTemplateRepository articleRepository;

    @Autowired
    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate, ArticleJdbcTemplateRepository articleRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.articleRepository = articleRepository;
    }

    private final RowMapper<Comment> commentRowMapper = (rs, rowNum) -> {
        Long articleId = rs.getLong("article_id");
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("not found article: " + articleId));

        return Comment.builder()
                .id(rs.getLong("id"))
                .content(rs.getString("content"))
                .password(rs.getString("password"))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .updatedAt(rs.getObject("updated_at", LocalDateTime.class))
                .article(article)
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
            String insertSql = "INSERT INTO comment (content, password, created_at, updated_at, article_id) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"id"});
                        LocalDateTime now = LocalDateTime.now();
                        ps.setString(1, comment.getContent());
                        ps.setString(2, comment.getPassword());
                        ps.setObject(3, now);
                        ps.setObject(4, now);
                        ps.setObject(5, comment.getArticle().getId());
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

    @Override
    public void deleteAllComments(Long articleId) {
        String sql = "DELETE FROM comment WHERE article_id = ?";
        jdbcTemplate.update(sql, articleId);
    }

}
