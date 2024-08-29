package com.elice.showpet.article.service;

import com.elice.showpet.article.entity.Article;
import com.elice.showpet.article.dto.CreateArticleDto;
import com.elice.showpet.article.dto.UpdateArticleDto;
import com.elice.showpet.comment.service.CommentViewService;
import com.elice.showpet.common.exception.BucketFileNotDeletedException;
import com.elice.showpet.common.exception.EntityNotFoundException;
import com.elice.showpet.article.repository.ArticleJdbcTemplateRepository;
import com.elice.showpet.article.repository.JdbcTemplateRepository;
import com.elice.showpet.aws.s3.service.S3BucketService;
import com.elice.showpet.category.entity.Category;
import com.elice.showpet.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleViewService {
    private final JdbcTemplateRepository articleRepository;

    private final S3BucketService s3BucketService;

    private final CategoryService categoryService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final CommentViewService commentViewService;

    private final int pageSize = 20;

    @Value("${spring.enabled.anon}")
    private boolean isEnabledAnon;

    @Autowired
    public ArticleViewService(
            ArticleJdbcTemplateRepository articleRepository,
            S3BucketService s3BucketService,
            CategoryService categoryService,
            CommentViewService commentViewService
    ) {
        this.articleRepository = articleRepository;
        this.s3BucketService = s3BucketService;
        this.categoryService = categoryService;
        this.commentViewService = commentViewService;
    }

    public boolean verifyPassword(Long articleId, String password) {
        if (password == null) return false;

        Article article = this.getArticle(articleId);
        return passwordEncoder.matches(password, article.getAnonPassword());
    }

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticle(Long id) throws EntityNotFoundException {
        return articleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Article not found"));
    }
    /**
     * pageSize는 20으로 할게요!
     * @param categoryId
     * @param page
     * @return 특정 게시판의 pagenated 된 게시글 목록을 가져옵니다.
     */
    public List<Article> getPagenatedArticles(Integer categoryId, Integer page) {
        return articleRepository.findPagenated(categoryId, page, pageSize);
    }

    public Article createArticle(CreateArticleDto articleDto) {
        if (articleDto.getAnonPassword() != null && isEnabledAnon) {
            articleDto.setAnonPassword(encryptPassword(articleDto.getAnonPassword()));
        }
        Article created = articleDto.toEntity();
        Category category = categoryService.findById(articleDto.getCategoryId());
        created.setCategory(category);
        return articleRepository.save(created);
    }

    public Article updateArticle(Long id, UpdateArticleDto articleDto) throws RuntimeException {
        try {
            Article findArticle = getArticle(id);
            Optional.ofNullable(articleDto.getImageDeleted()).flatMap(_ -> Optional.ofNullable(findArticle.getImage())).ifPresent((str) -> {
                try {
                    removeImage(str);
                } catch (BucketFileNotDeletedException e) {
                    throw new BucketFileNotDeletedException(e.getMessage());
                }
                findArticle.setImage(null);
            });
            Optional.ofNullable(articleDto.getTitle())
                    .ifPresent(findArticle::setTitle);
            Optional.ofNullable(articleDto.getContent())
                    .ifPresent(findArticle::setContent);
            Optional.ofNullable(articleDto.getImage())
                    .ifPresent((image) -> {
                        String findArticleImage = findArticle.getImage();
                        if (findArticleImage != null) {
                            try {
                                removeImage(findArticleImage);
                            } catch (RuntimeException e) {
                                throw new RuntimeException(e.getMessage());
                            }
                        }
                        findArticle.setImage(image);
                    });

            return articleRepository.save(findArticle);
        } catch (EntityNotFoundException | BucketFileNotDeletedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteAllArticlesRelatedWithCategory(Integer categoryId) {
        List<Article> articles = articleRepository.findPagenated(categoryId, 0, Integer.MAX_VALUE);
        articles.forEach((article) -> {
            deleteArticle(article.getId());
        });
    }

    /**
     * pageSize는 20으로 할게요!
     * @param categoryId
     * @param keyword
     * @param page
     * @return 해당 키워드를 가진 게시글 목록을 가져옵니다.
     */
    public List<Article> searchArticle(Integer categoryId, String keyword, int page) {
        List<Article> articles = articleRepository.search(categoryId, keyword, page, pageSize);
        return articles;
    }

    public Long deleteArticle(Long id) throws RuntimeException {
        try {
            Article article = getArticle(id);
            removeImage(article.getImage());
            commentViewService.deleteAllComments(id);
            articleRepository.delete(article);
            return article.getCategory().getId();
        } catch (EntityNotFoundException | BucketFileNotDeletedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void removeImage(String image) throws BucketFileNotDeletedException {
        try {
            s3BucketService.deleteFile(image);
        } catch (Exception e) {
            throw new BucketFileNotDeletedException("S3 버킷에서 이미지가 삭제되지 않았습니다.");
        }
    }
}
