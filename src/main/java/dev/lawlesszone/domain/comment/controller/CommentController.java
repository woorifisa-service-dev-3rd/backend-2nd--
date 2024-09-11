package dev.lawlesszone.domain.comment.controller;

import dev.lawlesszone.domain.comment.dto.CommentDTO;
import dev.lawlesszone.domain.comment.entity.Comment;
import dev.lawlesszone.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/articles/{articleId}/comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDTO> getComments(@PathVariable Long articleId) {
        return commentService.getComments(articleId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public CommentDTO addComment(Authentication authentication, @PathVariable Long articleId, CommentDTO commentDTO) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return commentService.saveComment(commentDTO, articleId, email);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{commentId}")
    public CommentDTO updateComment(@PathVariable Long articleId, @PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
        return commentService.updateComment(commentDTO, commentId);
    }
}
