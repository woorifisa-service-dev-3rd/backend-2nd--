package dev.lawlesszone.domain.comment.controller;

import dev.lawlesszone.domain.comment.entity.Comment;
import dev.lawlesszone.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/articles/view/{articleId}/comments")
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public String addComment(@PathVariable Long articleId, Comment comment) {
        commentService.saveComment(comment, articleId);
        return "redirect:/articles/view/" + articleId;
    }

    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/articles/view/" + articleId;
    }

    @PostMapping("/update/{commentId}")
    public String updateComment(@PathVariable Long articleId, @PathVariable Long commentId, Comment comment) {
        commentService.updateComment(comment, commentId);
        return "redirect:/articles/view/" + articleId;
    }
}
