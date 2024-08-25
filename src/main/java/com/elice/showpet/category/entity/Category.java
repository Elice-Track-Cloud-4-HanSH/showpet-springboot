package com.elice.showpet.category.entity;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.member.persistence.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 300)
  private String description;

  private String image;

  @OneToMany(
    mappedBy = "category",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  @ToString.Exclude
  private List<Article> articles = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "memberId")
  @ToString.Exclude
  private MemberEntity member;
}
