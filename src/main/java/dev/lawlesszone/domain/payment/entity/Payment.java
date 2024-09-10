package dev.lawlesszone.domain.payment.entity;

import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.global.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

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

    private boolean valid;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void addMember(Member member) {
        this.member = member;
        this.member.getPayment().add(this);
        this.member.payPremium();
    }

    public void invalidate() {
        this.valid = false;
        this.member.getPayment().remove(this);
        this.member.downgradePremium();
    }
}
