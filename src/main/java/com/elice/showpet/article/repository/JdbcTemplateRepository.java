package com.elice.showpet.article.repository;

import com.elice.showpet.article.entity.Article;

import java.util.List;
import java.util.Optional;

public interface JdbcTemplateRepository {

  List<Article> findAll();

  Optional<Article> findById(Long subjectId);

  Article save(Article subject);

  void delete(Article subject);
}
