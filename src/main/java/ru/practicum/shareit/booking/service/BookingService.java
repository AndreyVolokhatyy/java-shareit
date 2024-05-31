package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreatedDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;
import java.util.Map;

public interface BookingService {
    BookingCreatedDto addBooking(BookingDto bookingDto, Map<String, String> headers);

    BookingCreatedDto approveBooking(long bookingId, boolean status, Map<String, String> headers);

    BookingCreatedDto getBookingById(long bookingId, Map<String, String> headers);

    Collection<BookingCreatedDto> getUserBookings(State state, Map<String, String> headers);

    Collection<BookingCreatedDto> getAllBookingsByUserOwner(State state, Map<String, String> headers);

    Booking validateBooking(long bookingId);
}

