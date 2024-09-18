package dev.lawlesszone.domain.atricle.service;

import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.repository.MemberRepository;
import dev.lawlesszone.domain.atricle.dto.ArticleRequestDTO;
import dev.lawlesszone.domain.atricle.dto.ArticleResponseDTO;
import dev.lawlesszone.domain.atricle.entity.Article;
import dev.lawlesszone.domain.atricle.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public List<ArticleResponseDTO> findAllArticles() {
        List<Article> articles = articleRepository.findAll();
        
        List<ArticleResponseDTO> articleResponses = articles.stream()
                .map(ArticleResponseDTO::from).collect(Collectors.toList());
        return articleResponses;
    }

    public ArticleResponseDTO findArticleById(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(()-> new RuntimeException(id+"와 일치하는 Article이 없습니다!"));
        return ArticleResponseDTO.from(article);
    }

    @Transactional
    public ArticleResponseDTO saveArticle(ArticleRequestDTO articleRequestDTO) {
        Member author = memberRepository.findById(articleRequestDTO.getMemberId())
                .orElseThrow(()-> new RuntimeException(articleRequestDTO.getMemberId() + "에 해당하는 유저가 존재하지 않습니다."));
        Article article = Article.from(articleRequestDTO);
        article.setAuthor(author);

        Article savedArticle = articleRepository.save(article);

        return ArticleResponseDTO.from(savedArticle);
    }

    @Transactional
    public ArticleResponseDTO updateArticle(Long id, ArticleRequestDTO articleRequestDTO) {
        Article updateArticle = articleRepository.findById(id).orElseThrow(()-> new RuntimeException( id + "에 해당하는 Article이 존재하지 않습니다."));
        Article article = Article.from(articleRequestDTO);
        updateArticle.updateArticle(article);

        return ArticleResponseDTO.from(updateArticle);
    }

    @Transactional
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
