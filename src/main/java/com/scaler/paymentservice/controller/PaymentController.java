package com.scaler.paymentservice.controller;

import com.scaler.paymentservice.dto.PaymentLinkRequest;
import com.scaler.paymentservice.model.Product;
import com.scaler.paymentservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return paymentService.getProducts();
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createPaymentLink(@RequestBody PaymentLinkRequest paymentRequest) {
        String paymentLink = paymentService.createPaymentLink(paymentRequest);
        return ResponseEntity.ok(Map.of("url", paymentLink));
    }

    @PostMapping("/{message}")
    public void stripeResponse(@PathVariable("message") String message) {
        System.out.println(message);
    }
}
