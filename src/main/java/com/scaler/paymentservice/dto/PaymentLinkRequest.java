package com.scaler.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLinkRequest {
    private int customerId;
    private int orderId;
    private String productName;
    private Long doctorFee;
    private String doctorName;
}
