package dev.lawlesszone.domain.Member.entity;

import dev.lawlesszone.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;

import dev.lawlesszone.domain.atricle.entity.Article;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

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
