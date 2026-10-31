package com.ticketnest.event.entity;

public enum SeatStatus {
    AVAILABLE,
    LOCKED,     // held during checkout, not yet confirmed
    BOOKED
}
