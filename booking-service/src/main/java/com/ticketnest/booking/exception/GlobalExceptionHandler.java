package com.ticketnest.booking.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<String> handleSeatNotAvailable(FeignException.NotFound ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Seat is not available: " + ex.contentUTF8());
    }

    @ExceptionHandler(SeatServiceUnavailableException.class)
    public ResponseEntity<String> handleServiceDown(SeatServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }
}