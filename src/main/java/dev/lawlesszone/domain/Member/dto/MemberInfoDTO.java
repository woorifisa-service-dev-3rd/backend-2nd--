package dev.lawlesszone.domain.Member.dto;

import dev.lawlesszone.domain.Member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoDTO {
    private Long id;

    @Email
    private String email;

    private String nickName;

    static public MemberInfoDTO createUserInfoDTO(Member member) {
        return MemberInfoDTO.builder().email(member.getEmail()).nickName(member.getEmail()).id(member.getId()).build();
    }
}
