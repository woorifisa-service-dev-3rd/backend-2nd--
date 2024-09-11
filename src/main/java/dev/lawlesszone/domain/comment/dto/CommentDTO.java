package dev.lawlesszone.domain.comment.dto;

import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.atricle.entity.Article;
import dev.lawlesszone.domain.comment.entity.Comment;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    @NotNull
    private String content;

    private String nickName;

    @NotNull
    private Boolean isAnonymous;

    public static CommentDTO from(Comment comment) {
        return CommentDTO.builder()
                .content(comment.getContent())
                .isAnonymous(comment.getIsAnonymous())
                .build();
    }
}
