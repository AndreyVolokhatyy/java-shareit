package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.dto.BookingCreatedDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.heandler.exception.BadRequestException;
import ru.practicum.shareit.heandler.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    @Autowired
    private final BookingServiceImpl bookingService;

    @MockBean
    private final UserService userService;

    @MockBean
    private final ItemService itemRepository;

    @MockBean
    private final BookingStorage bookingRepository;

    private Map<String, String> headers;

    @BeforeEach
    void setUp() {
        headers = new HashMap<>();
        headers.put("x-sharer-user-id", "1");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("Accept", "application/json");
        headers.put("Content-Length", "164");
    }

    @Test
    void create() {
        LocalDateTime start = LocalDateTime.now().plusDays(1L);
        LocalDateTime end = LocalDateTime.now().plusDays(2L);

        BookingDto bookingDto = BookingDto.builder()
                .start(start)
                .end(end)
                .itemId(1L)
                .build();

        User owner = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        User owner1 = User.builder()
                .id(2L)
                .name("user2")
                .email("user2@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        when(itemRepository.getItem(any(), any(Long.class)))
                .thenReturn(item);

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        when(userService.getUsers())
                .thenReturn(List.of(userDto));

        when(userService.get(1L))
                .thenReturn(owner1);

        Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();

        when(bookingRepository.save(any()))
                .thenReturn(booking);

        BookingCreatedDto bookingCreatedDto = bookingService.addBooking(bookingDto, headers);
        assertThat(bookingCreatedDto, is(notNullValue()));
    }

    @Test
    void approve() {
        LocalDateTime start = LocalDateTime.now().plusDays(1L);
        LocalDateTime end = LocalDateTime.now().plusDays(2L);

        User owner = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        BookingCreatedDto bookingInfoDto = bookingService.approveBooking(1L, true, headers);
        assertThat(bookingInfoDto, is(notNullValue()));
    }

    @Test
    void approveNotOwner() {
        LocalDateTime start = LocalDateTime.now().plusDays(1L);
        LocalDateTime end = LocalDateTime.now().plusDays(2L);

        User owner = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(false)
                .user(owner)
                .build();

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.approveBooking(1L, true, headers));
    }

    @Test
    void approveApproved() {
        LocalDateTime start = LocalDateTime.now().plusDays(1L);
        LocalDateTime end = LocalDateTime.now().plusDays(2L);

        User owner = User.builder()
                .id(1L)
                .name("user3")
                .email("user3@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(false)
                .user(owner)
                .build();

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        Assertions.assertThrows(BadRequestException.class, () -> bookingService.approveBooking(1L, true, headers));
    }

    @Test
    void approveRejected() {
        LocalDateTime start = LocalDateTime.now().plusDays(1L);
        LocalDateTime end = LocalDateTime.now().plusDays(2L);

        User owner = User.builder()
                .id(1L)
                .name("user3")
                .email("user3@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.REJECTED)
                .build();

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        Assertions.assertThrows(BadRequestException.class, () -> bookingService.approveBooking(1L, false, headers));
    }

    @Test
    void getBookingById() {
        LocalDateTime start = LocalDateTime.now().plusDays(1L);
        LocalDateTime end = LocalDateTime.now().plusDays(2L);

        User owner = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        final Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        BookingCreatedDto bookingInfoDto = bookingService.getBookingById(1L, headers);
        assertThat(bookingInfoDto, is(notNullValue()));
    }

    @Test
    void getUserBookings() {
        LocalDateTime start = LocalDateTime.now().plusDays(1L);
        LocalDateTime end = LocalDateTime.now().plusDays(2L);

        User owner = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        final Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        when(bookingRepository.findAllByBookerIdOrderByStartDesc(any(Long.class), any()))
                .thenReturn(List.of(booking));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        List<BookingCreatedDto> bookingInfoDto = new ArrayList<>(bookingService.getUserBookings(State.ALL, headers, 1L, 100L));
        Assertions.assertEquals(bookingInfoDto.get(0).getId(), 1L);
    }

    @Test
    void getUserBookingsPast() {
        LocalDateTime start = LocalDateTime.now().minusDays(1L);
        LocalDateTime end = LocalDateTime.now().plusDays(1L);

        User owner = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        final Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        when(bookingRepository.findAllByEndBeforeAndBookerIdOrderByStartDesc(any(), any(Long.class), any()))
                .thenReturn(List.of(booking));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        List<BookingCreatedDto> bookingInfoDto = new ArrayList<>(bookingService.getUserBookings(State.PAST, headers, 1L, 100L));
        Assertions.assertEquals(bookingInfoDto.get(0).getId(), 1L);
    }

    @Test
    void getUserBookingsFuture() {
        LocalDateTime start = LocalDateTime.now().plusDays(10L);
        LocalDateTime end = LocalDateTime.now().plusDays(11L);

        User owner = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        final Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        when(bookingRepository.findAllByStartAfterAndBookerIdOrderByStartDesc(any(), any(Long.class), any()))
                .thenReturn(List.of(booking));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        List<BookingCreatedDto> bookingInfoDto = new ArrayList<>(bookingService.getUserBookings(State.FUTURE, headers, 1L, 100L));
        Assertions.assertEquals(bookingInfoDto.get(0).getId(), 1L);
    }

    @Test
    void getUserBookingsCurrent() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(11L);

        User owner = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        final Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        when(bookingRepository.findAllByEndAfterAndStartBeforeAndBookerIdOrderByStartDesc(any(), any(), any(Long.class), any()))
                .thenReturn(List.of(booking));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        List<BookingCreatedDto> bookingInfoDto = new ArrayList<>(bookingService.getUserBookings(State.CURRENT, headers, 1L, 100L));
        Assertions.assertEquals(bookingInfoDto.get(0).getId(), 1L);
    }

    @Test
    void getUserBookingsWaiting() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(11L);

        User owner = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        final Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(any(Long.class), any(), any()))
                .thenReturn(List.of(booking));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        List<BookingCreatedDto> bookingInfoDto = new ArrayList<>(bookingService.getUserBookings(State.WAITING, headers, 1L, 100L));
        Assertions.assertEquals(bookingInfoDto.get(0).getId(), 1L);
    }

    @Test
    void getUserBookingsRejected() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(11L);

        User owner = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        final Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.REJECTED)
                .build();

        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(any(Long.class), any(), any()))
                .thenReturn(List.of(booking));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        List<BookingCreatedDto> bookingInfoDto = new ArrayList<>(bookingService.getUserBookings(State.REJECTED, headers, 1L, 100L));
        Assertions.assertEquals(bookingInfoDto.get(0).getId(), 1L);
    }

    @Test
    void getAllBookingsByUserOwner() {
        LocalDateTime start = LocalDateTime.now().plusDays(1L);
        LocalDateTime end = LocalDateTime.now().plusDays(2L);

        User owner = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        final Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        Collection<BookingCreatedDto> bookingInfoDto = bookingService.getAllBookingsByUserOwner(State.ALL, headers, 1L, 100L);
        assertThat(bookingInfoDto, is(notNullValue()));
    }

    @Test
    void validateBooking() {
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.validateBooking(100L));
    }
}