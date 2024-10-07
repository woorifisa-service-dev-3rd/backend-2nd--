package dev.lawlesszone.domain.atricle.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleRequestDTO {
    @NotNull
    private Long memberId;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private Long viewCount;
}
