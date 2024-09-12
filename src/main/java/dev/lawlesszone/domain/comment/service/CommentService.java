package dev.lawlesszone.domain.comment.service;

import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.repository.MemberRepository;
import dev.lawlesszone.domain.atricle.entity.Article;
import dev.lawlesszone.domain.atricle.repository.ArticleRepository;
import dev.lawlesszone.domain.comment.dto.CommentRequestDTO;
import dev.lawlesszone.domain.comment.dto.CommentResponseDTO;
import dev.lawlesszone.domain.comment.entity.Comment;
import dev.lawlesszone.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    public List<CommentResponseDTO> getComments(Long articleId) {
       return commentRepository.findByArticleId(articleId)
               .stream().map(comment -> CommentResponseDTO.from(comment))
               .collect(Collectors.toList());
    }

    public CommentResponseDTO saveComment(CommentRequestDTO commentRequestDTO, Long articleId, String email) {
        Article findArticle = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException(articleId + "에 해당하는 Article이 존재하지 않습니다."));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(email + "에 해당하는 Member가 존재하지 않습니다."));

        Comment comment = Comment.of(commentRequestDTO, member, findArticle);
        return CommentResponseDTO.from(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponseDTO updateComment(CommentRequestDTO commentRequestDTO, Long commentId) {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException(commentId + "에 해당하는 Comment가 존재하지 않습니다."));
        findComment.setContent(commentRequestDTO.getContent());
        findComment.setAnonymous(commentRequestDTO.getIsAnonymous());
        return CommentResponseDTO.from(findComment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
