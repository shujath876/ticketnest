package com.ticketnest.booking.client;

import com.ticketnest.booking.client.EventServiceClient;
import com.ticketnest.booking.dto.SeatDto;
import com.ticketnest.booking.exception.SeatServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventServiceClientFallbackFactory implements FallbackFactory<EventServiceClient> {

    @Override
    public EventServiceClient create(Throwable cause) {
        return new EventServiceClient() {
            @Override
            public SeatDto lockSeat(Long seatId) {
                log.error("event-service unavailable while locking seat {}:{}",seatId,cause.getMessage());
                throw new SeatServiceUnavailableException("Could not lock seat " + seatId, cause);
            }

            @Override
            public SeatDto confirmSeat(Long seatId) { log.error("event-service unavailable while confirming seat {}: {}", seatId, cause.getMessage());
                throw new SeatServiceUnavailableException("Could not confirm seat " + seatId, cause);
            }

            @Override public SeatDto releaseSeat(Long seatId) {
                // TODO: this is the trickiest one — if release fails during saga // compensation, the seat can be stuck LOCKED forever. Worth a // retry queue or a scheduled reconciliation job rather than // just throwing here.
                log.error("event-service unavailable while releasing seat {}: {}", seatId, cause.getMessage());
                throw new SeatServiceUnavailableException("Could not release seat " + seatId, cause);
            }
            };
        }
    }