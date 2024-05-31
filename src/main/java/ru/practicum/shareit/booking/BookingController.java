package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreatedDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService service;

    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public BookingCreatedDto addBooking(@RequestHeader Map<String, String> headers,
                                        @Valid @RequestBody BookingDto bookingDto) {
        return service.addBooking(bookingDto, headers);
    }

    @PatchMapping("/{bookingId}")
    public BookingCreatedDto approveBooking(@RequestHeader Map<String, String> headers,
                                            @PathVariable long bookingId,
                                            @RequestParam(name = "approved") boolean status) {
        return service.approveBooking(bookingId, status, headers);
    }

    @GetMapping("/{bookingId}")
    public BookingCreatedDto getBookingById(@RequestHeader Map<String, String> headers,
                                            @PathVariable long bookingId) {
        return service.getBookingById(bookingId, headers);
    }

    @GetMapping
    public Collection<BookingCreatedDto> getAllBookingsByUser(@RequestHeader Map<String, String> headers,
                                                              @RequestParam(name = "state", defaultValue = "ALL") State state,
                                                              @RequestParam(defaultValue = "0") Long from,
                                                              @RequestParam(defaultValue = "10") Long size) {
        return service.getUserBookings(state, headers, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingCreatedDto> getBookingsByUser(@RequestHeader Map<String, String> headers,
                                                           @RequestParam(name = "state", defaultValue = "ALL") State state,
                                                           @RequestParam(defaultValue = "0") Long from,
                                                           @RequestParam(defaultValue = "10") Long size) {
        return service.getAllBookingsByUserOwner(state, headers, from, size);
    }
}
