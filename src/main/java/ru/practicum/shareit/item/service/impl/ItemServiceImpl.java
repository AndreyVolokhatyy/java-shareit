package ru.practicum.shareit.item.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.storage.CommentStorage;
import ru.practicum.shareit.heandler.exception.BadRequestException;
import ru.practicum.shareit.heandler.exception.NotFoundValueException;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.repository.ItemStorage;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemStorage itemStorage;
    private RequestService itemRequestService;
    private UserService userService;
    private BookingStorage bookingStorage;
    private CommentStorage commentStorage;

    public ItemServiceImpl(RequestService itemRequestService, UserService userService, ItemStorage itemStorage, BookingStorage bookingStorage, CommentStorage commentStorage) {
        this.itemRequestService = itemRequestService;
        this.userService = userService;
        this.itemStorage = itemStorage;
        this.bookingStorage = bookingStorage;
        this.commentStorage = commentStorage;
    }

    @Override
    public ItemCreatedDto createItem(Map<String, String> headers, ItemDto itemDto) {
        int userId = getUserId(headers);
        if (itemDto.getAvailable() == null
                || itemDto.getName() == null
                || itemDto.getName().isEmpty()
                || itemDto.getDescription() == null
        ) {
            throw new ValidationException();
        }
        if (!checkUser(userId)) {
            throw new NotFoundValueException();
        }
        Item item = ItemDto.toItem(itemDto);
        item.setUser(UserDto.toUser(userService.getUser(userId)));
        return ItemCreatedDto.toItemDto(itemStorage.save(item));
    }

    @Override
    public ItemCreatedDto updateItem(Map<String, String> headers, long itemId, ItemDto itemDto) {
        Item item = itemStorage.findById(itemId).orElseThrow(NotFoundValueException::new);
        int userId = getUserId(headers);
        if (!checkUser(userId) || item.getUser().getId() != userId) {
            throw new NotFoundValueException();
        }
        if (itemDto.getName() != null && !itemDto.getName().isEmpty()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemCreatedDto.toItemDto(itemStorage.save(item));
    }

    @Override
    public Item getItem(Map<String, String> headers, long id) {
        Item item = itemStorage.findById(id).orElseThrow(NotFoundValueException::new);
        int userId = getUserId(headers);
        if (!checkUser(userId)) {
            throw new NotFoundValueException();
        }
        return item;
    }

    @Override
    public ItemDto getItemDto(Map<String, String> headers, long id) {
        Item item = itemStorage.findById(id).orElseThrow(NotFoundValueException::new);
        int userId = getUserId(headers);
        if (!checkUser(userId)) {
            throw new NotFoundValueException();
        }
        ItemDto itemDto = getLastAndNextBookings(item, userId);
        itemDto.setId(item.getId());
        return itemDto;
    }

    @Override
    public Set<ItemDto> getItemUser(Map<String, String> headers) {
        int userId = getUserId(headers);
        return itemStorage.findAll().stream()
                .filter(i -> i.getUser().getId() == userId)
                .map(i -> getLastAndNextBookings(i, userId))
                .collect(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(ItemDto::getId)))
                );
    }

    @Override
    public Set<ItemCreatedDto> searchItem(String text) {
        if (text.isEmpty()) {
            return new HashSet<>();
        }
        return itemStorage.findAll().stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(ItemCreatedDto::toItemDto)
                .collect(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(ItemCreatedDto::getId)))
                );
    }

    private boolean checkUser(int id) {
        boolean check = false;
        for (User user : userService.getUsers().stream().map(UserDto::toUser).collect(Collectors.toList())) {
            if (user.getId() == id) {
                check = true;
                break;
            }
        }
        return check;
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentDto commentCreatedDto, long itemId, Map<String, String> headers) {
        Comment comment = CommentDto.toComment(commentCreatedDto);
        int userId = getUserId(headers);
        Item item = getItem(headers, itemId);
        User user = UserDto.toUser(userService.getUser(userId));
        List<Booking> bookings = bookingStorage.findByItemIdAndBookerId(itemId, userId);
        if (bookings.isEmpty()) {
            throw new BadRequestException("Только арендаторы могут оставлять отзыв!");
        }
        for (Booking booking : bookings) {
            if (Status.REJECTED == booking.getStatus()) {
                throw new BadRequestException("Нельзя оставить отзыв, есть аренда невозможна!");
            }
        }
        Optional<Booking> booking = bookings.stream().min(Comparator.comparing(Booking::getStart));
        if (booking.get().getStart().isAfter(comment.getCreated())) {
            throw new BadRequestException("Нельзя оставлять отзыв до аренды!");
        }
        comment.setItem(item);
        comment.setAuthor(user);
        return CommentDto.toCommentDto(commentStorage.save(comment));
    }

    private int getUserId(Map<String, String> headers) {
        return Integer.parseInt(headers.get("x-sharer-user-id"));
    }

    private ItemDto getLastAndNextBookings(Item item, long userId) {
        List<Booking> lastBookings = bookingStorage.findAllByItemIdAndEndBeforeOrderByEndDesc(
                item.getId(), LocalDateTime.now());
        List<Booking> nextBookings = bookingStorage.findAllByItemIdAndStartAfter(item.getId(), LocalDateTime.now());
        ItemDto itemDto = ItemDto.toItemDto(item);
        if (item.getUser().getId() == userId) {
            if (!lastBookings.isEmpty() && !nextBookings.isEmpty()) {
                Booking lastBooking = lastBookings.get(0);
                Booking nextBooking = nextBookings.get(nextBookings.size() - 1);
                itemDto.setLastBooking(BookingDto.toBookingDto(lastBooking));
                itemDto.setNextBooking(BookingDto.toBookingDto(nextBooking));
            } else {
                List<Booking> activeBookings =
                        bookingStorage.findAllByEndAfterAndStartBeforeAndItemUserIdOrderByStartDesc(
                                LocalDateTime.now(), LocalDateTime.now(), userId);
                for (Booking booking : activeBookings) {
                    if (booking.getItem().getId() == item.getId()) {
                        itemDto.setLastBooking(BookingDto.toBookingDto(booking));
                    }
                }
            }
        }
        itemDto.setComments(commentStorage.findAllByItemId(item.getId())
                .stream()
                .map(CommentDto::toCommentDto)
                .collect(Collectors.toList())
        );
        itemDto.setId(item.getId());
        return itemDto;
    }
}
