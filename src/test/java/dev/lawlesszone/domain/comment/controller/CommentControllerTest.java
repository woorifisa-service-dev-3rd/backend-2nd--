package dev.lawlesszone.domain.comment.controller;

import dev.lawlesszone.domain.comment.dto.CommentRequestDTO;
import dev.lawlesszone.domain.comment.dto.CommentResponseDTO;
import dev.lawlesszone.domain.comment.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentControllerTest {

    @Autowired
    CommentService commentService;

    @Test
    void addComment() {
        CommentRequestDTO comment = CommentRequestDTO.builder()
                .content("메롱")
                .isAnonymous(true)
                .build();
        Long articleId = 1l;
        String email = "testuser01@testmail.com";
        CommentResponseDTO newComment = commentService.saveComment(comment, articleId, email);

        assertThat(newComment.getNickName()).isEqualTo("박준현");
        assertThat(newComment.getContent()).isEqualTo("메롱");
        assertThat(newComment.getIsAnonymous()).isEqualTo(true);
    }
}