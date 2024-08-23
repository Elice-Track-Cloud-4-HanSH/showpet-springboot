package com.elice.showpet.article.repository;

import com.elice.showpet.article.entity.Article;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleJdbcTemplateRepository implements JdbcTemplateRepository {
  private final JdbcTemplate jdbcTemplate;

  public ArticleJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Article> articleRowMapper = (rs, rowNum) -> {
    return Article.builder()
            .id(rs.getLong("id"))
            .title(rs.getString("title"))
            .content(rs.getString("content"))
            .image(rs.getString("image"))
            .createdAt(rs.getObject("created_at", LocalDateTime.class))
            .updatedAt(rs.getObject("updated_at", LocalDateTime.class))
            .build();
  };

  @Override
  public List<Article> findAll() {
    String sql = "SELECT * FROM article";
    return jdbcTemplate.query(sql, articleRowMapper);
  }

  @Override
  public Optional<Article> findById(Long id) {
    String sql = "SELECT * FROM article WHERE id = ?";
    return jdbcTemplate.query(sql, new Object[]{id}, articleRowMapper).stream().findFirst();
  }

  @Override
  public Article save(Article article) {
    if (article.getId() == null) {
      String insertSql = "INSERT INTO article(title, content, image, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
      KeyHolder keyHolder = new GeneratedKeyHolder();

      jdbcTemplate.update(
        conn -> {
          PreparedStatement ps = conn.prepareStatement(insertSql, new String[]{"id"});
          LocalDateTime now = LocalDateTime.now();
          ps.setString(1, article.getTitle());
          ps.setString(2, article.getContent());
          ps.setString(3, article.getImage());
          ps.setObject(4, now);
          ps.setObject(5, now);
          return ps;
        }, keyHolder
      );
      Number key = keyHolder.getKey();
      if (key != null) article.setId(key.longValue());
    } else {
      String updateSql = "UPDATE article SET title = ?, content = ?, image = ?, updated_at = ? WHERE id = ?";
      jdbcTemplate.update(
            updateSql,
            article.getTitle(),
            article.getContent(),
            article.getImage(),
            LocalDateTime.now(),
            article.getId()
      );
    }
    return article;
  }

  @Override
  public void delete(Article article) {
    String sql = "DELETE FROM article WHERE id = ?";
    jdbcTemplate.update(sql, article.getId());
  }
}
