package dev.lawlesszone.domain.atricle.service;

import dev.lawlesszone.domain.atricle.entity.Article;
import dev.lawlesszone.domain.atricle.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> findAllArticles() {
        return articleRepository.findAll();
    }

    public Article findArticleById(Long id) {
        return articleRepository.findById(id).orElseThrow();
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    @Transactional
    public Article updateArticle(Article article, Long id) {
        Article findArticle = articleRepository.findById(id).orElseThrow();
        findArticle.setTitle(article.getTitle());
        findArticle.setContent(article.getContent());

        return findArticle;
    }

    @Transactional
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
