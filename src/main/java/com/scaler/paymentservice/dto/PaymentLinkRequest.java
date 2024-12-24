package com.scaler.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLinkRequest {
    private int customerId;
    private String customerName;
    private int productId;
    private int price;
}
