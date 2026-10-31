package com.ticketnest.booking.client;

import com.ticketnest.booking.dto.PaymentRequest;
import com.ticketnest.booking.dto.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Unlike EventServiceClientFallbackFactory, this one degrades gracefully:
 * it returns a PaymentResponse with status FAILED instead of throwing.
 *
 * Why the difference? The booking saga already has a compensation path for
 * "payment failed" (release the seat, mark booking CANCELLED) — that path
 * doesn't care *why* payment failed, whether it was a real decline or
 * payment-service being down/timing out/circuit open. Reusing that single
 * path keeps BookingService simple: it only ever has to handle
 * SUCCESS vs. not-SUCCESS, not a separate "service unreachable" case.
 *
 * Seat locking couldn't do this — a fake "locked" response would be a lie
 * about a shared resource's actual state, so that one has to throw instead.
 */
@Slf4j
@Component
public class PaymentServiceClientFallbackFactory implements FallbackFactory<PaymentServiceClient> {

    @Override
    public PaymentServiceClient create(Throwable cause) {
        return request -> {
            log.error("payment-service unavailable for booking {}: {}", request.getBookingId(), cause.getMessage());
            return new PaymentResponse(null, request.getBookingId(), request.getAmount(), "FAILED");
        };
    }
}
