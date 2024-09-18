package dev.lawlesszone.domain.atricle.dto;

import dev.lawlesszone.domain.atricle.entity.Article;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ArticleResponseDTO {
    private Long id;
    private Long memberId;
    private String authorNickName;
    private String title;
    private String content;
    private Long viewCount;

    public static ArticleResponseDTO from(Article article) {
        return ArticleResponseDTO.builder()
                .id(article.getId())
                .memberId(article.getAuthor().getId())
                .authorNickName(article.getAuthor().getNickName())
                .title(article.getTitle())
                .content(article.getContent())
                .viewCount(article.getViewCount())
                .build();
    }
}
