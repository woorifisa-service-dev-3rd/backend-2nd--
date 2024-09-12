package dev.lawlesszone.domain.payment.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PaymentDTO {
    private String impUid;
    private String merchantUid;
    private String amount;
}
