package dev.lawlesszone.domain.comment.service;

import dev.lawlesszone.domain.atricle.entity.Article;
import dev.lawlesszone.domain.atricle.repository.ArticleRepository;
import dev.lawlesszone.domain.comment.entity.Comment;
import dev.lawlesszone.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public Comment saveComment(Comment comment, Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow();
        comment.setArticle(findArticle);

        // TODO: 로그인 여부에 따라 설정
        comment.setIsAnonymous(true);
        return commentRepository.save(comment);
    }
}
