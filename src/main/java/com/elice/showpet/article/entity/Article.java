package com.elice.showpet.article.entity;

import com.elice.showpet.category.entity.Category;
import com.elice.showpet.comment.entity.Comment;
import com.elice.showpet.common.entity.BaseTimeEntity;
import com.elice.showpet.member.persistence.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Article extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(length = 1000)
    private String image;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    @ToString.Exclude
    private Category category;

    @OneToMany(
            mappedBy = "article",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    public static ArticleBuilder builder() {
        return new ArticleBuilder();
    }
}
