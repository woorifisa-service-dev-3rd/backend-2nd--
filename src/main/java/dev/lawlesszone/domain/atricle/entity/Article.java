package dev.lawlesszone.domain.atricle.entity;



import dev.lawlesszone.domain.comment.entity.Comment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    private Member author;

    private List<Comment> comments = new ArrayList<>();

    private Long viewCount;
}
