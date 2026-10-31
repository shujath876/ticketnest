package com.ticketnest.booking.controller;

import com.ticketnest.booking.dto.BookingRequest;
import com.ticketnest.booking.dto.BookingResponse;
import com.ticketnest.booking.entity.BookingStatus;
import com.ticketnest.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }

    @GetMapping("/{id}")
    public BookingResponse getBooking(@PathVariable("id") Long id) {
        return bookingService.getBooking(id);
    }

    @GetMapping
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable("id") Long id) {
        bookingService.deleteBooking(id);
    }

    @PatchMapping("/{id}/status")
    public BookingResponse updateStatus(@PathVariable("id") Long id,
                                        @RequestParam("status") BookingStatus status) {
        return bookingService.updateStatus(id, status);
    }
}
