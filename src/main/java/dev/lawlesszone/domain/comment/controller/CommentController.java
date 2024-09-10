package dev.lawlesszone.domain.comment.controller;

import dev.lawlesszone.domain.comment.dto.CommentDTO;
import dev.lawlesszone.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/articles/view/{articleId}/comments")
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public String addComment(@PathVariable Long articleId, CommentDTO comment) {
        commentService.saveComment(comment, articleId);
        return "redirect:/articles/view/" + articleId;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/articles/view/" + articleId;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{commentId}")
    public String updateComment(@PathVariable Long articleId, @PathVariable Long commentId, CommentDTO comment) {
        commentService.updateComment(comment, commentId);
        return "redirect:/articles/view/" + articleId;
    }
}
