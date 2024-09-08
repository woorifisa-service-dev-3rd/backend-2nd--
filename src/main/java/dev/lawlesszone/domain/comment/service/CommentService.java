package dev.lawlesszone.domain.comment.service;

import dev.lawlesszone.domain.atricle.entity.Article;
import dev.lawlesszone.domain.atricle.repository.ArticleRepository;
import dev.lawlesszone.domain.comment.dto.CommentDTO;
import dev.lawlesszone.domain.comment.entity.Comment;
import dev.lawlesszone.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public void saveComment(CommentDTO comment, Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow();
        comment.setArticle(findArticle);
        // TODO: 로그인 여부에 따라 설정
        comment.setIsAnonymous(true);

        commentRepository.save(Comment.builder().content(comment.getContent()).article(comment.getArticle()).isAnonymous(comment.getIsAnonymous()).build());
    }

    @Transactional
    public void updateComment(CommentDTO comment, Long CommentId) {
        Comment findComment = commentRepository.findById(CommentId).orElseThrow();
        findComment.setContent(comment.getContent());
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
