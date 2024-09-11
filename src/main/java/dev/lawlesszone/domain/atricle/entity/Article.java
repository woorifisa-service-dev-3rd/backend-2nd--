package dev.lawlesszone.domain.atricle.entity;

import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.comment.entity.Comment;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    private Long viewCount;

    public String setTitle(String title) {
        return this.title = title;
    }

    public String setContent(String content) {
        return this.content = content;
    }
}
