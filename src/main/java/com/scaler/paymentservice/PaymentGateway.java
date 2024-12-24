package com.scaler.paymentservice;

import com.scaler.paymentservice.dto.PaymentLinkRequest;

public interface PaymentGateway {
    String createPaymentLink(PaymentLinkRequest paymentLinkRequest);
}
