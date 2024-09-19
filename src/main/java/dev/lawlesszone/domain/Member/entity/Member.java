package dev.lawlesszone.domain.Member.entity;

import dev.lawlesszone.domain.comment.entity.Comment;
import dev.lawlesszone.domain.payment.entity.Payment;
import lombok.*;


import dev.lawlesszone.domain.atricle.entity.Article;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "payment")
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Builder.Default
    private String authorities="ROLE_USER";

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Article> articles;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Comment> comments;

    private String nickName;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    @Builder.Default
    private List<Payment> payment=new ArrayList<>();

    private int premium;

    public void downgradePremium(){
        if(premium<30){
            this.premium=0;
        }
        else{
            this.premium-=30;
        }
    }
    public void payPremium(){
        this.premium+=30;
    }
    public void decreaseDailyPremium(){
        this.premium--;
    }
}
