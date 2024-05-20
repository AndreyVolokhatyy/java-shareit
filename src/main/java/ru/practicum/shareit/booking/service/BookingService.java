package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;
import java.util.Map;

public interface BookingService {
    Booking addBooking(BookingDto bookingDto, Map<String, String> headers);

    Booking approveBooking(long bookingId, boolean status, Map<String, String> headers);

    Booking getBookingById(long bookingId, Map<String, String> headers);

    Collection<Booking> getUserBookings(String state, Map<String, String> headers);

    Collection<Booking> getAllBookingsByUserOwner(String state, Map<String, String> headers);

    Booking validateBooking(long bookingId);
}

