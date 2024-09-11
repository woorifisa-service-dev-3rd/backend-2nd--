package dev.lawlesszone.domain.atricle.dto;

import dev.lawlesszone.domain.atricle.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ArticleWrtieResponseDTO {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private Long viewCount;

    public static ArticleWrtieResponseDTO from(Article article) {
        return ArticleWrtieResponseDTO.builder()
                .id(article.getId())
                .memberId(article.getAuthor().getId())
                .title(article.getTitle())
                .content(article.getContent())
                .viewCount(article.getViewCount())
                .build();
    }
}
