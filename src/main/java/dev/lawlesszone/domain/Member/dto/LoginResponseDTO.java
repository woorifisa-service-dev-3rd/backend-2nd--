package dev.lawlesszone.domain.Member.dto;

import dev.lawlesszone.domain.Member.entity.Member;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private Long id;

    @Email
    private String email;

    private String nickName;

    LoginResponseDTO(Member member) {
        this.email = member.getEmail();
        this.nickName = member.getNickName();
    }

    public static LoginResponseDTO createLoginResponseDTO(Member member) {
        return LoginResponseDTO.builder().id(member.getId()).email(member.getEmail()).nickName(member.getNickName()).build();
    }
}