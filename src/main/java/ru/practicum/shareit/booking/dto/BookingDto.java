package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;

@Data
public class BookingDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private int idItem;
    private int booker;
    private String status;

    public BookingDto(LocalDateTime start, LocalDateTime end, int idItem, int booker, String status) {
        this.start = start;
        this.end = end;
        this.idItem = idItem;
        this.booker = booker;
        this.status = status;
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getStart(),
                booking.getEnd(),
                booking.getIdItem(),
                booking.getBooker(),
                booking.getStatus());
    }
}
