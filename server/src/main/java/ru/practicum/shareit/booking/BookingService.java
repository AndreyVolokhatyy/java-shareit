package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingCutDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Map;

public interface BookingService {

    BookingDto create(BookingInputDto bookingDto, Map<String, String> headers);

    BookingDto update(Long bookingId, Map<String, String> headers, Boolean approved);

    BookingDto getBookingById(Long bookingId, Map<String, String> headers);

    List<BookingDto> getBookings(BookingState state, Map<String, String> headers, Integer from, Integer size);

    List<BookingDto> getBookingsOwner(BookingState state, Map<String, String> headers, Integer from, Integer size);

    BookingCutDto getLastBooking(Long itemId);

    BookingCutDto getNextBooking(Long itemId);

    Booking getBookingWithUserBookedItem(Long userId, Long itemId);

}
