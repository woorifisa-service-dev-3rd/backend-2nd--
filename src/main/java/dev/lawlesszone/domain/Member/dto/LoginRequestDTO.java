package dev.lawlesszone.domain.Member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDTO {
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String password;

}