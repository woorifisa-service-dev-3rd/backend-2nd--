package dev.lawlesszone.domain.atricle.dto;

import dev.lawlesszone.domain.atricle.entity.Article;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ArticleViewResponseDTO {
    private Long id;
    private String authorNickName;
    private String title;
    private String content;

    public static ArticleViewResponseDTO from (Article article) {
        return ArticleViewResponseDTO.builder()
                .id(article.getId())
                .authorNickName(article.getAuthor().getNickName())
                .title(article.getTitle())
                .content(article.getContent())
                .build();
    }
}
