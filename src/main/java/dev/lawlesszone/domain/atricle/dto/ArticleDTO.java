package dev.lawlesszone.domain.atricle.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ArticleDTO {
    private String title;
    private String content;
}
