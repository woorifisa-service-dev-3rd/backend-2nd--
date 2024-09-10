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
public class SignupResponseDTO {
    private Long id;

    @Email
    private String email;

    private String nickName;

    public static SignupResponseDTO fromEntity(Member member) {
        return SignupResponseDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .build();
    }

}
