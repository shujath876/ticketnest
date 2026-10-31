package com.ticketnest.event.dto;

import com.ticketnest.event.entity.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatDto {
    private Long id;
    private String seatNumber;
    private Double price;
    private SeatStatus status;
    private Long eventId;
}
