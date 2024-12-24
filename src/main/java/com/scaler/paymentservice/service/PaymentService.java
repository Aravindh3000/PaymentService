package com.scaler.paymentservice.service;

import com.scaler.paymentservice.PaymentGateway;
import com.scaler.paymentservice.dto.PaymentLinkRequest;
import com.scaler.paymentservice.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService{

    public final PaymentGateway paymentGateway;
    private static final List<Product> productList = new ArrayList<>();

    public PaymentService(PaymentGateway paymentGateway){
        this.paymentGateway = paymentGateway;
        initializeProducts();
    }
    public String createPaymentLink(PaymentLinkRequest paymentLinkRequest) {
        return paymentGateway.createPaymentLink(paymentLinkRequest);
    }

    public static void initializeProducts() {
        productList.add(new Product(1, "Laptop", 80000.00));
        productList.add(new Product(2, "Smartphone", 50000.00));
        productList.add(new Product(3, "Headphones", 2000.00));
    }

    public List<Product> getProducts() {
        return productList;
    }

    public void handlePayment(String payload) {
        paymentGateway.handlePayment(payload);
    }
}
