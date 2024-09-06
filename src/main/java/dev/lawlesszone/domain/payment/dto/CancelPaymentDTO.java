package dev.lawlesszone.domain.payment.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CancelPaymentDTO {
    private String code;
    private String message;
}
