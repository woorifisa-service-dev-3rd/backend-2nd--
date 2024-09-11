package dev.lawlesszone.domain.Member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    @NotNull
    private String GrantType;

    @NotNull
    private String accessToken;

    public static TokenDTO fromString(String accessToken) {
        return TokenDTO.builder()
                .GrantType("Bearer")
                .accessToken(accessToken)
                .build();
    }
}
