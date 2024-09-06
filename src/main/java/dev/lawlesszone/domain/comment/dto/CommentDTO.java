package dev.lawlesszone.domain.comment.dto;

import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.atricle.entity.Article;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private String content;
    private Member author;
    private Boolean isAnonymous;
    private Article article;
}
