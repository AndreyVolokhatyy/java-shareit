package ru.practicum.shareit.booking.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.heandler.exception.BadRequestException;
import ru.practicum.shareit.heandler.exception.InternalServerErrorException;
import ru.practicum.shareit.heandler.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingStorage bookingStorage;

    public BookingServiceImpl(ItemService itemService, UserService userService, BookingStorage bookingStorage) {
        this.itemService = itemService;
        this.userService = userService;
        this.bookingStorage = bookingStorage;
    }

    @Override
    @Transactional
    public Booking addBooking(BookingDto bookingDto, Map<String, String> headers) {
        long userId = getUserId(headers);
        Booking booking = BookingDto.toBooking(bookingDto);
        Item item = itemService.getItem(headers, bookingDto.getItemId());
        User user = userService.getUser(userId);
        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("Вещь с ID %d нельзя арендовать", item.getId()));
        }
        if (bookingDto.getStart() == null
                || bookingDto.getEnd() == null) {
            throw new BadRequestException("Неверная дата бронировния");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())
                || bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BadRequestException("Неверная дата бронировния");
        }
        if (user.equals(item.getUser())) {
            throw new NotFoundException("Владелец вещи не может ее забронировать!");
        }
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setBooker(user);
        bookingStorage.save(booking);
        return booking;
    }

    @Override
    @Transactional
    public Booking approveBooking(long bookingId, boolean status, Map<String, String> headers) {
        Booking booking = validateBooking(bookingId);
        long userId = getUserId(headers);
        if (booking.getItem().getUser().getId() != userId) {
            throw new NotFoundException("Подтверждение брони может быть выполнено только владельцем!");
        }
        if (status) {
            if (booking.getStatus() == Status.APPROVED) {
                throw new BadRequestException("Нельзя подтверить бронь после подтверждения!");
            }
            booking.setStatus(Status.APPROVED);
        } else {
            if (booking.getStatus() == Status.REJECTED) {
                throw new BadRequestException("Нельзя отклонить бронь после отклонения!");
            }
            booking.setStatus(Status.REJECTED);
        }
        bookingStorage.save(booking);
        return booking;
    }

    @Override
    public Booking getBookingById(long bookingId, Map<String, String> headers) {
        Booking booking = validateBooking(bookingId);
        return checkBookingOwnerOrItemOwner(booking, getUserId(headers));
    }

    @Override
    public Collection<Booking> getUserBookings(String state, Map<String, String> headers) {
        long userId = userService.getUser(getUserId(headers)).getId();
        switch (state) {
            case "ALL":
                return bookingStorage.findAllByBookerIdOrderByStartDesc(userId);
            case "PAST":
                return bookingStorage.findAllByEndBeforeAndBookerIdOrderByStartDesc(LocalDateTime.now(), userId);
            case "FUTURE":
                return bookingStorage.findAllByStartAfterAndBookerIdOrderByStartDesc(LocalDateTime.now(), userId);
            case "CURRENT":
                return bookingStorage.findAllByEndAfterAndStartBeforeAndBookerIdOrderByStartDesc(
                        LocalDateTime.now(), LocalDateTime.now(), userId);
            case "WAITING":
                return bookingStorage.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case "REJECTED":
                return bookingStorage.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            default:
                throw new InternalServerErrorException("Unknown state: " + state);
        }
    }

    @Override
    public Collection<Booking> getAllBookingsByUserOwner(String state, Map<String, String> headers) {
        long userId = userService.getUser(getUserId(headers)).getId();
        switch (state) {
            case "ALL":
                return bookingStorage.findAllByItemUserIdOrderByStartDesc(userId);
            case "PAST":
                return bookingStorage.findByEndBeforeAndItemUserIdOrderByStartDesc(LocalDateTime.now(), userId);
            case "FUTURE":
                return bookingStorage.findAllByItemUserIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            case "CURRENT":
                return bookingStorage.findAllByEndAfterAndStartBeforeAndItemUserIdOrderByStartDesc(
                        LocalDateTime.now(), LocalDateTime.now(), userId);
            case "WAITING":
                return bookingStorage.findAllByItemUserIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case "REJECTED":
                return bookingStorage.findAllByItemUserIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            default:
                throw new InternalServerErrorException("Unknown state: " + state);
        }
    }

    @Override
    public Booking validateBooking(long bookingId) {
        return bookingStorage.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format("Бронь с ID %d не найдена", bookingId)));
    }

    private Booking checkBookingOwnerOrItemOwner(Booking booking, long userId) {
        if (booking.getItem().getUser().getId() == userId || booking.getBooker().getId() == userId) {
            return booking;
        }
        throw new NotFoundException("Только владелец брони или вещи может получить данные!");
    }

    private int getUserId(Map<String, String> headers) {
        return Integer.parseInt(headers.get("x-sharer-user-id"));
    }
}
