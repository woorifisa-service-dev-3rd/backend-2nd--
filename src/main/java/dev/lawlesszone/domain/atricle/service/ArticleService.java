package dev.lawlesszone.domain.atricle.service;

import dev.lawlesszone.domain.atricle.entity.Article;
import dev.lawlesszone.domain.atricle.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
