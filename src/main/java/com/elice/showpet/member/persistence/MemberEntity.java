package com.elice.showpet.member.persistence;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.category.entity.Category;
import com.elice.showpet.comment.entity.Comment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberEntity implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 20)
  private String loginId;

  @Column(nullable = false)
  private String loginPw;

  @Column(nullable = false, unique = true, length = 20)
  private String nickname;

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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("user"));
  }

  @Override
  public String getPassword() {
    return loginPw;
  }

  @Override
  public String getUsername() {
    return loginId;
  }
}
