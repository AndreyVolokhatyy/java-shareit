package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.service = bookingService;
    }

    @ResponseBody
    @PostMapping
    public BookingDto create(@RequestBody BookingInputDto bookingInputDto,
                             @RequestHeader Map<String, String> headers) {
        return service.create(bookingInputDto, headers);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId,
                             @RequestHeader Map<String, String> headers, @RequestParam Boolean approved) {
        return service.update(bookingId, headers, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId, @RequestHeader Map<String, String> headers) {
        return service.getBookingById(bookingId, headers);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                        @RequestHeader Map<String, String> headers,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(required = false) Integer size) {
        return service.getBookings(state, headers, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsOwner(@RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                             @RequestHeader Map<String, String> headers,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(required = false) Integer size) {
        return service.getBookingsOwner(state, headers, from, size);
    }
}
