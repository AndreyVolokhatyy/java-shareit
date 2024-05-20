package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BookingDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private int idItem;
    private int booker;
    private String status;

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .start(booking.getStart())
                .end(booking.getEnd())
                .idItem(booking.getIdItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }
}
