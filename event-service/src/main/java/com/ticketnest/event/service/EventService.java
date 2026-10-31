package com.ticketnest.event.service;

import com.ticketnest.event.dto.EventDto;
import com.ticketnest.event.dto.SeatDto;
import com.ticketnest.event.entity.Event;
import com.ticketnest.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventDto createEvent(Event event) {
        if (event.getSeats() != null) {
            event.getSeats().forEach(seat -> seat.setEvent(event));
        }
        return toDto(eventRepository.save(event));
    }

    private EventDto toDto(Event event) {
        List<SeatDto> seatDtos = event.getSeats().stream()
                .map(s -> new SeatDto(s.getId(), s.getSeatNumber(), s.getPrice(),
                        s.getStatus(), event.getId()))
                .toList();
        return new EventDto(event.getId(), event.getName(), event.getVenue(),
                event.getEventTime(), seatDtos);
    }

    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found: " + id));
    }

    public List<EventDto> getAllEvents() {

        return eventRepository.findAll().stream().map(this::toDto).toList();
    }
}
