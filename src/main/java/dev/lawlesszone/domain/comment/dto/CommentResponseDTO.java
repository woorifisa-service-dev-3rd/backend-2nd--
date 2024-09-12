package dev.lawlesszone.domain.comment.dto;

import dev.lawlesszone.domain.comment.entity.Comment;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {

    private String content;

    private String nickName;

    private String email;

    private Boolean isAnonymous;

    public static CommentResponseDTO from(Comment comment) {
        return CommentResponseDTO.builder()
                .content(comment.getContent())
                .nickName((comment.getAuthor() != null) ? comment.getAuthor().getNickName() : null)
                .email((comment.getAuthor() != null) ? comment.getAuthor().getEmail() : null)
                .isAnonymous(comment.getIsAnonymous())
                .build();
    }
}
