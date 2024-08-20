package com.elice.showpet.member.entity;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.category.entity.Category;
import com.elice.showpet.comment.entity.Comment;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false, unique = true, length = 20)
  String login_id;

  @Column(nullable = false)
  String login_pw;

  @Column(nullable = false, unique = true, length = 20)
  String nickname;

  String email;

  @OneToMany(mappedBy = "member")
  @ToString.Exclude
  private List<Category> categories = new ArrayList<>();

  @OneToMany(
    mappedBy = "member",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  @ToString.Exclude
  private List<Article> articles = new ArrayList<>();

  @OneToMany(
    mappedBy = "member",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  @ToString.Exclude
  private List<Comment> comments = new ArrayList<>();
}
