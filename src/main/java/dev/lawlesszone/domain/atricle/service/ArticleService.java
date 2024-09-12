package dev.lawlesszone.domain.atricle.service;

import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.repository.MemberRepository;
import dev.lawlesszone.domain.atricle.dto.ArticleWriteRequestDTO;
import dev.lawlesszone.domain.atricle.dto.ArticleViewResponseDTO;
import dev.lawlesszone.domain.atricle.dto.ArticleWrtieResponseDTO;
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

    public List<ArticleViewResponseDTO> findAllArticles() {
        List<Article> articles = articleRepository.findAll();
        
        List<ArticleViewResponseDTO> articleResponses = articles.stream()
                .map(ArticleViewResponseDTO::from).collect(Collectors.toList());
        return articleResponses;
    }

    public ArticleViewResponseDTO findArticleById(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(()-> new RuntimeException(id+"와 일치하는 Article이 없습니다!"));
        return ArticleViewResponseDTO.from(article);
    }

    @Transactional
    public ArticleWrtieResponseDTO saveArticle(ArticleWriteRequestDTO articleWriteRequestDTO) {
        Member author = memberRepository.findById(articleWriteRequestDTO.getMemberId())
                .orElseThrow(()-> new RuntimeException(articleWriteRequestDTO.getMemberId() + "에 해당하는 유저가 존재하지 않습니다."));
        Article article = Article.from(articleWriteRequestDTO);
        article.setAuthor(author);

        Article savedArticle = articleRepository.save(article);

         return ArticleWrtieResponseDTO.from(savedArticle);
    }

    @Transactional
    public ArticleWrtieResponseDTO updateArticle(Long id, ArticleWriteRequestDTO articleWriteRequestDTO) {
        Article updateArticle = articleRepository.findById(id).orElseThrow(()-> new RuntimeException( id + "에 해당하는 Article이 존재하지 않습니다."));
        Article article = Article.from(articleWriteRequestDTO);
        updateArticle.updateArticle(article);

        return ArticleWrtieResponseDTO.from(updateArticle);
    }

    @Transactional
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
