package dev.lawlesszone.domain.Member.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String confirmPassword;

    private String nickName;
}
