package dev.lawlesszone.domain.Member.dto;

import dev.lawlesszone.domain.Member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDTO {
    @Email
    private String email;

    private String nickName;

    LoginResponseDTO(Member member) {
        this.email = member.getEmail();
        this.nickName = member.getNickName();
    }

    public static LoginResponseDTO createLoginResponseDTO(Member member) {
        return new LoginResponseDTO(member);
    }
}