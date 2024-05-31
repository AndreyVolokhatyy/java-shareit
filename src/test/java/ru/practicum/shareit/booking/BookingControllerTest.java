package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreatedDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private BookingDto bookingDto;

    private BookingCreatedDto bookingCreatedDto;

    private ItemDto item;

    private UserDto booker;

    private LocalDateTime start;

    private LocalDateTime end;

    private Map<String, String> headers;

    @BeforeEach
    void setUp() {
        headers = new HashMap<>();
        headers.put("x-sharer-user-id", "1");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("Accept", "application/json");
        headers.put("Content-Length", "126");

        start = LocalDateTime.now().plusDays(1L);

        end = LocalDateTime.now().plusDays(2L);

        bookingDto = BookingDto.builder()
                .start(start)
                .end(end)
                .itemId(1L)
                .build();

        item = ItemDto.builder()
                .id(1L)
                .name("item created")
                .build();

        booker = UserDto.builder()
                .id(1L)
                .build();

        bookingCreatedDto = BookingCreatedDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(ItemDto.toItem(item))
                .booker(UserDto.toUser(booker))
                .status(Status.WAITING)
                .build();
    }

    @AfterEach
    void tearDown() {
        start = null;
        end = null;
        bookingDto = null;
        item = null;
        booker = null;
    }

    @Test
    void addBookingTest() throws Exception {
        when(bookingService.addBooking(any(), any()))
                .thenReturn(bookingCreatedDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-sharer-user-id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingCreatedDto.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(bookingCreatedDto.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingCreatedDto.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.item.id", is(bookingCreatedDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingCreatedDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingCreatedDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingCreatedDto.getStatus().toString())));
    }

    @Test
    void approve() throws Exception {
        bookingDto.setStatus(Status.APPROVED);

        when(bookingService.approveBooking(any(Long.class), any(Boolean.class), any()))
                .thenReturn(bookingCreatedDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("x-sharer-user-id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingCreatedDto.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(bookingCreatedDto.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingCreatedDto.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.item.id", is(bookingCreatedDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingCreatedDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingCreatedDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingCreatedDto.getStatus().toString())));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBookingById(any(Long.class), any()))
                .thenReturn(bookingCreatedDto);

        mvc.perform(get("/bookings/1")
                        .header("x-sharer-user-id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingCreatedDto.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(bookingCreatedDto.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingCreatedDto.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.item.id", is(bookingCreatedDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingCreatedDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingCreatedDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingCreatedDto.getStatus().toString())));
    }

    @Test
    void getAll() throws Exception {
        when(bookingService.getUserBookings(any(), any(), any(Long.class), any(Long.class)))
                .thenReturn(List.of(bookingCreatedDto));

        mvc.perform(get("/bookings/")
                        .header("x-sharer-user-id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingCreatedDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start",
                        is(bookingCreatedDto.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.[0].end", is(bookingCreatedDto.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.[0].item.id", is(bookingCreatedDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].item.name", is(bookingCreatedDto.getItem().getName())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingCreatedDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].status", is(bookingCreatedDto.getStatus().toString())));
    }

    @Test
    void getByOwner() throws Exception {
        when(bookingService.getAllBookingsByUserOwner(any(), any(), any(Long.class), any(Long.class)))
                .thenReturn(List.of(bookingCreatedDto));

        mvc.perform(get("/bookings/owner")
                        .header("x-sharer-user-id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingCreatedDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start",
                        is(bookingCreatedDto.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.[0].end", is(bookingCreatedDto.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.[0].item.id", is(bookingCreatedDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].item.name", is(bookingCreatedDto.getItem().getName())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingCreatedDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].status", is(bookingCreatedDto.getStatus().toString())));
    }
}