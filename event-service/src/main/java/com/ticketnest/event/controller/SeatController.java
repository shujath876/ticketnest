package com.ticketnest.event.controller;

import com.ticketnest.event.dto.SeatDto;
import com.ticketnest.event.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/{eventId}/seats")
    public List<SeatDto> getSeats(
            @PathVariable("eventId") Long eventId) {

        return seatService.getSeatsForEvent(eventId);
    }

    @PostMapping("/seats/{seatId}/lock")
    public SeatDto lockSeat(@PathVariable("seatId") Long seatId) {
        return seatService.lockSeat(seatId);
    }

    @PostMapping("/seats/{seatId}/confirm")
    public SeatDto confirmSeat(@PathVariable("seatId") Long seatId) {
        return seatService.confirmSeat(seatId);
    }

    @PostMapping("/seats/{seatId}/release")
    public SeatDto releaseSeat(@PathVariable("seatId") Long seatId) {
        return seatService.releaseSeat(seatId);
    }
}
