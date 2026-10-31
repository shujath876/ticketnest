package com.ticketnest.booking.client;

import com.ticketnest.booking.dto.SeatDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "event-service", fallbackFactory = EventServiceClientFallbackFactory.class)
public interface EventServiceClient {

    @PostMapping("/api/events/seats/{seatId}/lock")
    SeatDto lockSeat(@PathVariable("seatId") Long seatId);

    @PostMapping("/api/events/seats/{seatId}/confirm")
    SeatDto confirmSeat(@PathVariable("seatId") Long seatId);

    @PostMapping("/api/events/seats/{seatId}/release")
    SeatDto releaseSeat(@PathVariable("seatId") Long seatId);
}
