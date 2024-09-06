package dev.lawlesszone.domain.Member.entity;

import dev.lawlesszone.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;

import dev.lawlesszone.domain.atricle.entity.Article;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Lazy
    @OneToMany(mappedBy = "author")
    private List<Article> articles;

    @Lazy
    @OneToMany(mappedBy = "author")
    private List<Comment> comments;

    private String nickName;

}
