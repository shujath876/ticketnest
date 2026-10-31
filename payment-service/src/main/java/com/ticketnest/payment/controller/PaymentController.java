package com.ticketnest.payment.controller;

import com.ticketnest.payment.dto.PaymentRequest;
import com.ticketnest.payment.dto.PaymentResponse;
import com.ticketnest.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse processPayment(@RequestBody PaymentRequest request) {
        return paymentService.processPayment(request);
    }
}
