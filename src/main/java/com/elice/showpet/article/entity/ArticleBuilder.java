package com.elice.showpet.article.entity;

import com.elice.showpet.category.entity.Category;
import com.elice.showpet.comment.entity.Comment;
import com.elice.showpet.member.persistence.MemberEntity;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleBuilder {

    private Long id;
    private String title;
    private String content;
    private String image;
    private MemberEntity member;
    private Category category;
    private List<Comment> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String anonPassword;

    public ArticleBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ArticleBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ArticleBuilder content(String content) {
        this.content = content;
        return this;
    }

    public ArticleBuilder image(String image) {
        this.image = image;
        return this;
    }

    public ArticleBuilder member(MemberEntity member) {
        this.member = member;
        return this;
    }

    public ArticleBuilder category(Category category) {
        this.category = category;
        return this;
    }

    public ArticleBuilder comments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public ArticleBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ArticleBuilder updatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public ArticleBuilder anonPassword(String anonPassword) {
        this.anonPassword = anonPassword;
        return this;
    }

    public Article build() {
        Article article = new Article();
        article.setId(id);
        article.setTitle(title);
        article.setContent(content);
        article.setImage(image);
        article.setMember(member);
        article.setCategory(category);
        article.setComments(comments);
        article.setCreatedAt(createdAt);
        article.setUpdatedAt(updatedAt);
        article.setAnonPassword(anonPassword);
        return article;
    }
}
