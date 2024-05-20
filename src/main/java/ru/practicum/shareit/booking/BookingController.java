package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
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
    public Booking addBooking(@RequestHeader Map<String, String> headers,
                              @Valid @RequestBody BookingDto bookingDto) {
        return service.addBooking(bookingDto, headers);
    }

    @PatchMapping("/{bookingId}")
    public Booking approveBooking(@RequestHeader Map<String, String> headers,
                                  @PathVariable long bookingId,
                                  @RequestParam(name = "approved") boolean status) {
        return service.approveBooking(bookingId, status, headers);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader Map<String, String> headers,
                                  @PathVariable long bookingId) {
        return service.getBookingById(bookingId, headers);
    }

    @GetMapping
    public Collection<Booking> getAllBookingsByUser(@RequestHeader Map<String, String> headers,
                                                    @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return service.getUserBookings(state, headers);
    }

    @GetMapping("/owner")
    public Collection<Booking> getBookingsByUser(@RequestHeader Map<String, String> headers,
                                                 @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return service.getAllBookingsByUserOwner(state, headers);
    }
}
