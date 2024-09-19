package dev.lawlesszone.domain.atricle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
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
