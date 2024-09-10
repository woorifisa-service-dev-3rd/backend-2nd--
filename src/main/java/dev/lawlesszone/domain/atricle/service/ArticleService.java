package dev.lawlesszone.domain.atricle.service;

import dev.lawlesszone.domain.atricle.dto.ArticleDTO;
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

    public Article saveArticle(ArticleDTO articleDTO) {
        Article saveArticle = Article.builder().title(articleDTO.getTitle()).content(articleDTO.getContent()).build();
        return articleRepository.save(saveArticle);
    }

    @Transactional
    public void updateArticle(ArticleDTO articleDTO, Long id) {
        Article findArticle = articleRepository.findById(id).orElseThrow();
        findArticle.setTitle(articleDTO.getTitle());
        findArticle.setContent(articleDTO.getContent());
    }

    @Transactional
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
