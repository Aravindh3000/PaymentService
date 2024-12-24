package com.scaler.paymentservice.stripe;

import com.google.gson.JsonSyntaxException;
import com.scaler.paymentservice.Constants;
import com.scaler.paymentservice.PaymentGateway;
import com.scaler.paymentservice.dto.PaymentLinkRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;

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
                    "product_data", Map.of("name", "Product " + paymentLinkRequest.getProductId()),
                    "unit_amount", paymentLinkRequest.getPrice()
            ));
            lineItem.put("quantity", 1);

            // Optionally add customer data in metadata if needed
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("customer_name", paymentLinkRequest.getCustomerId());
            params.put("metadata", metadata);

            params.put("line_items", List.of(lineItem));
            params.put("mode", "payment");

            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
           throw new RuntimeException(e.getMessage());
        }

    }

    // Using the Spark framework (http://sparkjava.com)
    @Override
    public void handlePayment(String payload) {
        Event event = null;

        try {
            event = ApiResource.GSON.fromJson(payload, Event.class);
        } catch (JsonSyntaxException e) {
            // Invalid payload
            throw new RuntimeException(e.getMessage());
        }

        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }

        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                // Then define and call a method to handle the successful payment intent.
                // handlePaymentIntentSucceeded(paymentIntent);
                System.out.println("Payment Intent succeeded");
                break;
            case "checkout.session.completed":
                Session session = (Session) stripeObject;
                System.out.println("Checkout session completed");
                // Then define and call a method to handle the successful attachment of a PaymentMethod.
                // handlePaymentMethodAttached(paymentMethod);
                break;
            case "charge.succeeded":
                Charge charge = (Charge) stripeObject;
                System.out.println("Charge succeeded");
                break;
            case "charge.failed":
                Charge chargeFailed = (Charge) stripeObject;
                System.out.println("Charge failed");
                break;
            case "charge.updated":
                Charge chargeUpdated = (Charge) stripeObject;
                System.out.println("Charge updated");
                break;
            // ... handle other event types
            default:
                System.out.println("Unhandled event type: " + event.getType());
                throw new RuntimeException("Unhandled event type: " + event.getType());
        }

    }
}
