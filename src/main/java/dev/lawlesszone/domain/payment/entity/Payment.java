package dev.lawlesszone.domain.payment.entity;

import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.global.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString(exclude = "member")
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String merchantUid;

    @Setter
    private boolean valid;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        this.member.getPayment().add(this);
    }
}
