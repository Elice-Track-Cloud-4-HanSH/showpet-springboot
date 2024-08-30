package com.elice.showpet.comment.entity;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.member.persistence.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"member", "article"})
@EntityListeners(AuditingEntityListener.class)
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 1000)
  private String content;

  @Column(length = 10)
  private String password;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private MemberEntity member;

  @ManyToOne
  @JoinColumn(name = "article_id", nullable = false)
  private Article article;
}
