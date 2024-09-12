package dev.lawlesszone.domain.comment.entity;

import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.atricle.entity.Article;
import dev.lawlesszone.domain.comment.dto.CommentRequestDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;

    private Boolean isAnonymous;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public static Comment of(CommentRequestDTO comment, Member member, Article article) {
        return Comment.builder()
                .content(comment.getContent())
                .author(member)
                .article(article)
                .isAnonymous(comment.getIsAnonymous())
                .build();
    }

    public void setArticle(Article article) {
        if (this.article.getComments() != null) {
            this.article.getComments().remove(this);
        }

        this.article = article;
        article.getComments().add(this);
    }

    public void setContent(String content) {
        this.content = content;
    }
}
