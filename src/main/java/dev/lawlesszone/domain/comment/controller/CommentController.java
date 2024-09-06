package dev.lawlesszone.domain.comment.controller;

import dev.lawlesszone.domain.comment.entity.Comment;
import dev.lawlesszone.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/articles/view/{articleId}")
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public String addComment(@PathVariable Long articleId, @ModelAttribute Comment comment) {
        commentService.saveComment(comment, articleId);
        return "redirect:/articles/view/" + articleId;
    }
}
