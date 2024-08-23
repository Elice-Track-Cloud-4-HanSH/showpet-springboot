package com.elice.showpet.comment.entity;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.member.entity.Member;
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
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 1000)
  private String content;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  // 테스트 시에는 userId의 nullable을 true로 수정!
  @ManyToOne
  @JoinColumn(name = "userId")
  @ToString.Exclude
  private Member member;

  @ManyToOne
  @JoinColumn(name = "articleId", nullable = false)
  @ToString.Exclude
  private Article article;
}
