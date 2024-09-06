package dev.lawlesszone.domain.Member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;

import dev.lawlesszone.domain.atricle.entity.Article;

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

    @OneToMany
    private Article[] articles;

    @OneToMany
    private Comment[] comments;

    private String nickName;

}
