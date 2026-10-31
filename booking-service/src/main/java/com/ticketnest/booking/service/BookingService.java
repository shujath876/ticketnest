package com.ticketnest.booking.service;

import com.ticketnest.booking.client.EventServiceClient;
import com.ticketnest.booking.client.PaymentServiceClient;
import com.ticketnest.booking.dto.BookingRequest;
import com.ticketnest.booking.dto.BookingResponse;
import com.ticketnest.booking.dto.PaymentRequest;
import com.ticketnest.booking.dto.PaymentResponse;
import com.ticketnest.booking.entity.Booking;
import com.ticketnest.booking.entity.BookingStatus;
import com.ticketnest.booking.exception.SeatServiceUnavailableException;
import com.ticketnest.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CRUD-only for now. This does NOT talk to event-service or payment-service
 * yet — createBooking() just persists a row as PENDING. The saga (lock seat
 * -> charge payment -> confirm/compensate) gets layered on top of this once
 * the plain CRUD skeleton across all services is confirmed working.
 */
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventServiceClient eventServiceClient;
    private final PaymentServiceClient paymentServiceClient;

    public BookingResponse createBooking(BookingRequest request) {

        // 1. Lock the seat first — nothing gets persisted until this succeeds.
        //    If this throws (seat unavailable, or event-service down via the
        //    fallback factory), it propagates straight out of this method —
        //    no booking row is ever created for a failed attempt.
        eventServiceClient.lockSeat(request.getSeatId());

        // 2. Seat is locked → now it's safe to create the booking as PENDING.
        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .eventId(request.getEventId())
                .seatId(request.getSeatId())
                .amount(request.getAmount())
                .status(BookingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        booking = bookingRepository.save(booking); // reassigned to get the generated id

        PaymentRequest paymentRequest = new PaymentRequest(booking.getId(), booking.getAmount());
        PaymentResponse paymentResponse = paymentServiceClient.processPayment(paymentRequest);

        // 4. Branch on the single outcome that matters: did payment succeed?
        if ("SUCCESS".equals(paymentResponse.getStatus())) {
            eventServiceClient.confirmSeat(request.getSeatId());
            booking.setStatus(BookingStatus.CONFIRMED);
        } else {
            eventServiceClient.releaseSeat(request.getSeatId()); // compensation
            booking.setStatus(BookingStatus.CANCELLED);
        }

        return toDto(bookingRepository.save(booking));
    }
    public BookingResponse getBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + id));
        return toDto(booking);
    }

   public List<BookingResponse> getAllBookings(){
        return bookingRepository.findAll().stream().map(this::toDto).toList();
   }

    public BookingResponse updateStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + id));
        booking.setStatus(status);
        return toDto(bookingRepository.save(booking));
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    private BookingResponse toDto(Booking booking) {
        return new BookingResponse(booking.getId(), booking.getUserId(), booking.getEventId(),
                booking.getSeatId(), booking.getAmount(), booking.getStatus());
    }
}
