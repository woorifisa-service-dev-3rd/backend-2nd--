package dev.lawlesszone.domain.payment.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class PreparationRequest {
    private String userId;
    private String merchantUid;
    private BigDecimal totalPrice;
}
