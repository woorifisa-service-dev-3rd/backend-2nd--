package dev.lawlesszone.domain.comment.dto;

import dev.lawlesszone.domain.comment.entity.Comment;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDTO {

    @NotNull
    private String content;

    @NotNull
    private Boolean isAnonymous;

    public static CommentRequestDTO from(Comment comment) {
        return CommentRequestDTO.builder()
                .content(comment.getContent())
                .isAnonymous(comment.getIsAnonymous())
                .build();
    }
}
