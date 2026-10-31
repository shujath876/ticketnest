package com.ticketnest.booking.dto;

import com.ticketnest.booking.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private Long userId;
    private Long eventId;
    private Long seatId;
    private Double amount;
    private BookingStatus status;
}
