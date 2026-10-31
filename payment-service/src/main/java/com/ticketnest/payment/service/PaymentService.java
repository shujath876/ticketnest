package com.ticketnest.payment.service;

import com.ticketnest.payment.dto.PaymentRequest;
import com.ticketnest.payment.dto.PaymentResponse;
import com.ticketnest.payment.entity.Payment;
import com.ticketnest.payment.entity.PaymentStatus;
import com.ticketnest.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    // TODO: replace with a real payment gateway integration (Stripe/Razorpay).
    // For now this always succeeds so the saga's happy path can be built and
    // tested first; wire up the FAILED branch once the gateway call exists,
    // so you can test the booking-service compensation (seat release) logic.
    public PaymentResponse processPayment(PaymentRequest request) {
        Payment payment = Payment.builder()
                .bookingId(request.getBookingId())
                .amount(request.getAmount())
                .status(PaymentStatus.SUCCESS)
                .processedAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);
        return new PaymentResponse(saved.getId(), saved.getBookingId(), saved.getAmount(), saved.getStatus());
    }
}
