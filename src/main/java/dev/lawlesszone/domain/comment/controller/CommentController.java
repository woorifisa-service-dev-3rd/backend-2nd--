package dev.lawlesszone.domain.comment.controller;

import dev.lawlesszone.domain.Member.dto.CustomUserDetail;
import dev.lawlesszone.domain.comment.dto.CommentRequestDTO;
import dev.lawlesszone.domain.comment.dto.CommentResponseDTO;
import dev.lawlesszone.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/articles/{articleId}/comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentResponseDTO> getComments(@PathVariable Long articleId) {
        return commentService.getComments(articleId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public CommentResponseDTO addComment(@AuthenticationPrincipal CustomUserDetail customUserDetail, @PathVariable Long articleId, @RequestBody CommentRequestDTO commentRequestDTO) {
        String email = customUserDetail.getEmail();
        return commentService.saveComment(commentRequestDTO, articleId, email);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{commentId}")
    public CommentResponseDTO updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDTO commentRequestDTO) {
        return commentService.updateComment(commentRequestDTO, commentId);
    }
}
