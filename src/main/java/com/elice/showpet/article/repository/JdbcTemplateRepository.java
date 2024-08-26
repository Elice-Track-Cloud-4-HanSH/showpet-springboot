package com.elice.showpet.article.repository;

import com.elice.showpet.article.entity.Article;

import java.util.List;
import java.util.Optional;

public interface JdbcTemplateRepository {

  List<Article> findAll();

  List<Article> findPagenated(int categoryId, int page, int pageSize);

  Optional<Article> findById(Long id);

  Article save(Article article);

  void delete(Article article);
}
