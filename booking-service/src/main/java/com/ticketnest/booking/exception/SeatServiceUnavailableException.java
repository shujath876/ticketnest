package com.ticketnest.booking.exception;

public class SeatServiceUnavailableException extends RuntimeException {
    public SeatServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
