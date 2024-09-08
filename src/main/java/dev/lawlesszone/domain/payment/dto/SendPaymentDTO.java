package dev.lawlesszone.domain.payment.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SendPaymentDTO {
    private Long id;
    private boolean valid;
}
