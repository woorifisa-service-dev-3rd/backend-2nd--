package dev.lawlesszone.domain.comment.entity;

import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.atricle.entity.Article;
import lombok.*;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

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
}
