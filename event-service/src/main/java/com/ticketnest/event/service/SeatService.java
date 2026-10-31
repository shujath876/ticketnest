package com.ticketnest.event.service;

import com.ticketnest.event.dto.SeatDto;
import com.ticketnest.event.entity.Seat;
import com.ticketnest.event.entity.SeatStatus;
import com.ticketnest.event.exception.ResourceNotFoundException;
import com.ticketnest.event.repository.SeatRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class SeatService {

   private final SeatRepository seatRepository;

   public  List<SeatDto> getSeatsForEvent(Long eventId){
       return seatRepository.findByEventId(eventId).stream()
               .map(this::toDto).toList();
   }
   public SeatDto lockSeat(Long seatId){
       Seat seat = seatRepository.findById(seatId)
               .orElseThrow(()->new ResourceNotFoundException("Seat not found: "+seatId));
       if(seat.getStatus() != SeatStatus.AVAILABLE){
           throw  new ResourceNotFoundException("Seat: "+seatId+" is not available");
       }
       seat.setStatus(SeatStatus.LOCKED);
       try {
           Seat saved = seatRepository.save(seat);
           return  toDto(saved);

       }catch (OptimisticLockingFailureException e){
           throw  new IllegalStateException("Seat "+seat+" was just taken by another request.");
       }
   }
   public SeatDto confirmSeat(Long seatId){
       Seat seat = seatRepository.findById(seatId).orElseThrow(()->new RuntimeException("Seat not found: "+seatId));
       seat.setStatus(SeatStatus.BOOKED);
       return  toDto(seatRepository.save(seat));

   }
   public SeatDto releaseSeat(Long seatId){
       Seat seat = seatRepository.findById(seatId)
               .orElseThrow(() -> new RuntimeException("Seat not found: " + seatId));
       if(seat.getStatus()!=SeatStatus.LOCKED){
           throw new IllegalStateException(   "Seat " + seatId + " is not locked and cannot be confirmed");
       }
       seat.setStatus(SeatStatus.AVAILABLE);

       return toDto(seatRepository.save(seat));
   }
   private SeatDto toDto(Seat seat){
       return new SeatDto(seat.getId(), seat.getSeatNumber(), seat.getPrice(),seat.getStatus()
       ,seat.getEvent().getId());
   }





}
