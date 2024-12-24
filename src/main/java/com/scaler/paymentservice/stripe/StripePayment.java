package com.scaler.paymentservice.stripe;

import com.scaler.paymentservice.Constants;
import com.scaler.paymentservice.PaymentGateway;
import com.scaler.paymentservice.dto.PaymentLinkRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StripePayment implements PaymentGateway {
    @Override
    public String createPaymentLink(PaymentLinkRequest paymentLinkRequest) {
        try{
            // Creating a Checkout Session
            Map<String, Object> params = new HashMap<>();
            params.put("success_url", Constants.successUrl);
            params.put("cancel_url", Constants.cancelUrl);
            params.put("payment_method_types", List.of("card"));

            Map<String, Object> lineItem = new HashMap<>();
            lineItem.put("price_data", Map.of(
                    "currency", "usd",
                    "product_data", Map.of("name", paymentLinkRequest.getDoctorName()),
                    "unit_amount", paymentLinkRequest.getDoctorFee()
            ));
            lineItem.put("quantity", 1);

            params.put("line_items", List.of(lineItem));
            params.put("mode", "payment");

            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
           throw new RuntimeException(e.getMessage());
        }

    }
}
