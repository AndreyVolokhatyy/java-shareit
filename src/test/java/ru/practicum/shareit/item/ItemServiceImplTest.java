package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentStorage;
import ru.practicum.shareit.heandler.exception.NotFoundValueException;
import ru.practicum.shareit.heandler.exception.PaginationException;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.item.service.repository.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.repository.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {

    @Autowired
    private final ItemServiceImpl itemService;

    @MockBean
    private final UserService userService;

    @MockBean
    private final UserStorage userStorage;

    @MockBean
    private final ItemStorage itemRepository;

    @MockBean
    private final BookingStorage bookingRepository;

    @MockBean
    private final CommentStorage commentRepository;

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
        User owner = User.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        when(userService.getUsers())
                .thenReturn(List.of(userDto));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        Item item = Item.builder()
                .id(1L)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .user(owner)
                .build();

        when(itemRepository.save(any()))
                .thenReturn(item);

        ItemCreatedDto itemCreatedDto = itemService.createItem(headers, itemDto);

        assertThat(itemCreatedDto, is(notNullValue()));
    }

    @Test
    void throwNotFoundException() {
        when(userStorage.findById(1L))
                .thenReturn(Optional.empty());

        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemService.createItem(headers, itemDto));

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemService.updateItem(headers, 1L, itemDto));

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemService.getItem(headers, 1L));

        CommentDto commentDto = CommentDto.builder()
                .text("comment")
                .build();

        User owner = User.builder()
                .id(2L)
                .name("user2")
                .email("user2@email.com")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        when(userStorage.findById(3L))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemService.addComment(commentDto, 1L, headers));
    }

    @Test
    void throwValidationException() {
        ItemDto itemDto = ItemDto.builder()
                .description("description")
                .available(true)
                .build();

        Assertions.assertThrows(ValidationException.class,
                () -> itemService.createItem(headers, itemDto));
    }

    @Test
    void throwNotFoundValueException() {
        ItemDto itemDto = ItemDto.builder()
                .name("asd")
                .description("description")
                .available(true)
                .build();

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemService.createItem(headers, itemDto));
    }

    @Test
    void update() {
        User owner = User.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        when(userService.getUsers())
                .thenReturn(List.of(userDto));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("nameUpdated")
                .description("descriptionUpdated")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        Item itemUpdated = Item.builder()
                .id(1L)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(true)
                .user(owner)
                .build();
        when(itemRepository.save(any()))
                .thenReturn(itemUpdated);

        ItemCreatedDto itemCreatedDto = itemService.updateItem(headers, 1L, itemDto);
        assertThat(itemCreatedDto, is(notNullValue()));
    }

    @Test
    void updateNotFoundValueException() {
        ItemDto itemDto = ItemDto.builder()
                .name("nameUpdated")
                .description("descriptionUpdated")
                .build();

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemService.updateItem(headers, 1L, itemDto));
    }

    @Test
    void updateAll() {
        User owner = User.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        when(userService.getUsers())
                .thenReturn(List.of(userDto));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        ItemDto itemDto = ItemDto.builder()
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        Item itemUpdated = Item.builder()
                .id(1L)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(true)
                .user(owner)
                .build();

        when(itemRepository.save(any()))
                .thenReturn(itemUpdated);

        ItemCreatedDto itemCreatedDto = itemService.updateItem(headers, 1L, itemDto);
        assertThat(itemCreatedDto, is(notNullValue()));
    }

    @Test
    void getItemDto() {

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        UserDto userDtoSecond = UserDto.builder()
                .id(2L)
                .name("name")
                .email("user@email.com")
                .build();

        User owner = User.builder()
                .id(2L)
                .name("user2")
                .email("user2@email.com")
                .build();

        User booker = User.builder()
                .id(3L)
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

        LocalDateTime created = LocalDateTime.now();
        Comment comment = Comment.builder()
                .id(1L)
                .text("text")
                .item(item)
                .author(booker)
                .created(created)
                .build();
        List<Comment> commentList = List.of(comment);

        ItemDto itemDto;

        when(userService.getUsers())
                .thenReturn(List.of(userDto, userDtoSecond));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        when(userService.getUser(2L))
                .thenReturn(userDto);

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        when(commentRepository.findAllByItemId(1L))
                .thenReturn(commentList);

        itemDto = itemService.getItemDto(headers, 1L);
        assertThat(itemDto, is(notNullValue()));
    }

    @Test
    void getItem() {

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        User owner = User.builder()
                .id(1L)
                .name("user")
                .email("user@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        when(userService.getUsers())
                .thenReturn(List.of(userDto));

        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));

        Item itemReturned = itemService.getItem(headers, 1L);
        Assertions.assertEquals(itemReturned.getId(), 1L);
    }

    @Test
    void getItemNotFound() {

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        User owner = User.builder()
                .id(1L)
                .name("user")
                .email("user@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        when(itemRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(NotFoundValueException.class, () -> itemService.getItem(headers, 1L));
    }

    @Test
    void getAll() {
        User owner = User.builder()
                .id(1L)
                .name("user2")
                .email("user2@email.com")
                .build();

        when(userStorage.findById(2L))
                .thenReturn(Optional.of(owner));

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        when(itemRepository.findAll())
                .thenReturn(List.of(item));

        Set<ItemDto> itemDtos = itemService.getItemUser(headers);
        Assertions.assertFalse(itemDtos.isEmpty());
    }

    @Test
    void search() {
        User owner = User.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        when(userService.getUsers())
                .thenReturn(List.of(userDto));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        Set<ItemCreatedDto> itemDtos = itemService.searchItem("");
        Assertions.assertTrue(itemDtos.isEmpty());

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        when(itemRepository.findAll())
                .thenReturn(List.of(item));
        itemDtos = itemService.searchItem("description");
        assertThat(itemDtos, is(notNullValue()));
    }

    @Test
    void comment() {
        User owner = User.builder()
                .id(1L)
                .name("user")
                .email("user@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        when(userService.getUsers())
                .thenReturn(List.of(userDto));

        when(userService.getUser(1L))
                .thenReturn(userDto);

        LocalDateTime created = LocalDateTime.now();

        Booking booking = Booking.builder()
                .id(1L)
                .start(created.minusMonths(5))
                .end(created.minusMonths(4))
                .item(item)
                .booker(booker)
                .status(Status.APPROVED)
                .build();
        when(bookingRepository
                .findByItemIdAndBookerId(any(Long.class), any(Long.class))
        ).thenReturn(List.of(booking));

        Comment comment = Comment.builder()
                .id(1L)
                .text("text")
                .item(item)
                .author(booker)
                .created(created)
                .build();
        when(commentRepository.save(any()))
                .thenReturn(comment);

        CommentDto commentDto = CommentDto.builder()
                .text("comment")
                .build();
        commentDto = itemService.addComment(commentDto, 1L, headers);
        assertThat(commentDto, is(notNullValue()));
    }

    @Test
    void throwNotFoundValueExceptionComment() {
        User owner = User.builder()
                .id(2L)
                .name("user2")
                .email("user2@email.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        User booker = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@email.com")
                .build();
        when(userStorage.findById(3L))
                .thenReturn(Optional.of(booker));

        LocalDateTime created = LocalDateTime.now();

        when(bookingRepository
                .findById(any(Long.class))
        ).thenReturn(Optional.empty());

        CommentDto commentDto = CommentDto.builder()
                .text("comment")
                .build();

        Assertions.assertThrows(NotFoundValueException.class, () -> itemService.addComment(commentDto, 1L, headers));
    }
}