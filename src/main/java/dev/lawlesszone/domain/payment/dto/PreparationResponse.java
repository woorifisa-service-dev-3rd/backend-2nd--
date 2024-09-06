package dev.lawlesszone.domain.payment.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PreparationResponse {
    private String merchantUid;
}
