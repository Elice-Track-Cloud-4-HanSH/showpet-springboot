package com.elice.showpet.article.entity;

import com.elice.showpet.category.entity.Category;
import com.elice.showpet.comment.entity.Comment;
import com.elice.showpet.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
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
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String title;

  @Column(nullable = false, length = 10000)
  private String content;

  @Column(length = 1000)
  private String image;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "userId")
  @ToString.Exclude
  private Member member;

  @ManyToOne
  @JoinColumn(name = "categoryId")
  @ToString.Exclude
  private Category category;

  @OneToMany(
    mappedBy = "article",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  @ToString.Exclude
  private List<Comment> comments = new ArrayList<>();
}
