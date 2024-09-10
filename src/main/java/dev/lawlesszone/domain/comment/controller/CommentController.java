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
    public CommentDTO addComment(Authentication authentication, @PathVariable Long articleId, CommentDTO comment) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        CommentDTO commentDTO = commentService.saveComment(comment, articleId, email);
        return commentDTO;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{commentId}")
    public String updateComment(@PathVariable Long articleId, @PathVariable Long commentId, CommentDTO comment) {
        commentService.updateComment(comment, commentId);
        return "redirect:/articles/view/" + articleId;
    }
}
