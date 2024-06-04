package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.repository.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.repository.UserStorage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIntegrationTest {

    @Autowired
    private final ItemService itemService;

    @Autowired
    private final UserStorage userRepository;

    @Autowired
    private final ItemStorage itemRepository;

    @Autowired
    private final BookingStorage bookingRepository;

    private Map<String, String> headers;

    @BeforeEach
    void setUp() {
        User owner = User.builder()
                .name("owner")
                .email("owner@email.com")
                .build();
        userRepository.save(owner);

        Item item = Item.builder()
                .name("item1")
                .description("item1 desc")
                .available(true)
                .user(owner)
                .build();

        User booker = User.builder()
                .name("booker")
                .email("booker@email.com")
                .build();

        LocalDateTime created = LocalDateTime.now();

        Booking lastBooking = Booking.builder()
                .start(created.minusMonths(5))
                .end(created.minusMonths(4))
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();

        Booking nextBooking = Booking.builder()
                .start(created.plusDays(1L))
                .end(created.plusDays(2L))
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(lastBooking);
        bookingRepository.save(nextBooking);

        headers = new HashMap<>();
        headers.put("x-sharer-user-id", "2");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("Accept", "application/json");
        headers.put("Content-Length", "164");
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DirtiesContext
    @Test
    void create() {
        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        ItemCreatedDto itemCreatedDto = itemService.createItem(headers, itemDto);

        assertThat(itemCreatedDto, is(notNullValue()));
        assertThat(itemCreatedDto.getId(), is(2L));
    }
}