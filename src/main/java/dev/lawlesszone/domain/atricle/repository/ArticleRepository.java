package dev.lawlesszone.domain.atricle.repository;

import dev.lawlesszone.domain.atricle.dto.ArticleDTO;
import dev.lawlesszone.domain.atricle.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
