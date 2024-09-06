package dev.lawlesszone.domain.Member.entity;

import dev.lawlesszone.domain.comment.entity.Comment;
import lombok.*;


import dev.lawlesszone.domain.atricle.entity.Article;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Article> articles;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Comment> comments;

    private String nickName;


}
