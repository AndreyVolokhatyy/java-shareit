package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.heandler.exception.NotFoundException;
import ru.practicum.shareit.heandler.exception.NotFoundValueException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.repository.ItemStorage;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.repository.UserStorage;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceImplTest {

    @Autowired
    private final RequestServiceImpl itemRequestService;

    @MockBean
    private final RequestRepository itemRequestRepository;

    @MockBean
    private final UserStorage userRepository;

    @MockBean
    private final ItemStorage itemRepository;

    private Map<String, String> headers;

    @BeforeEach
    void setUp() {
        headers = new HashMap<>();
        headers.put("x-sharer-user-id", "2");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("Accept", "application/json");
        headers.put("Content-Length", "82");
    }

    @Test
    void create() {
        User requester = User.builder()
                .id(2L)
                .name("name")
                .email("user@email.com")
                .build();

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(requester));

        RequestDto requestDto = RequestDto.builder()
                .description("description")
                .build();

        LocalDateTime now = LocalDateTime.now();

        Request request = Request.builder()
                .id(1L)
                .description("description")
                .requester(requester)
                .created(now)
                .build();

        when(itemRequestRepository.save(any()))
                .thenReturn(request);

        RequestDto itemRequestDtoCreated = itemRequestService.create(headers, requestDto);
        assertThat(itemRequestDtoCreated, is(notNullValue()));
    }

    @Test
    void throwUserNotFoundException() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        RequestDto requestDto = RequestDto.builder()
                .description("description")
                .build();

        NotFoundException invalidUserIdException;

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemRequestService.create(headers, requestDto));

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemRequestService.get(headers));

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemRequestService.get(headers, 0L, 10L));

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemRequestService.get(headers, 1L));
    }

    @Test
    void getRequestListRelatedToRequester() {
        User requester = User.builder()
                .id(2L)
                .name("name2")
                .email("user2@email.com")
                .build();

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(requester));

        when(itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(2L))
                .thenReturn(new ArrayList<>());

        List<RequestDto> itemRequestDtos = itemRequestService.get(headers);

        assertTrue(itemRequestDtos.isEmpty());

        LocalDateTime requestCreationDate = LocalDateTime.now();

        Request request = Request.builder()
                .id(1L)
                .description("description")
                .requester(requester)
                .created(requestCreationDate)
                .build();

        List<Request> itemRequests = List.of(request);

        when(itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(2L))
                .thenReturn(itemRequests);

        User owner = User.builder()
                .id(1L)
                .name("name1")
                .email("user1@email.com")
                .build();

        List<Item> items = Collections.emptyList();

        when(itemRepository.findAllByRequestIdIn(List.of(1L)))
                .thenReturn(items);

        itemRequestDtos = itemRequestService.get(headers);

        assertTrue(itemRequestDtos.get(0).getItems().isEmpty());

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .request(request)
                .build();

        items = List.of(item);

        when(itemRepository.findAllByRequestIdIn(List.of(1L)))
                .thenReturn(items);

        itemRequestDtos = itemRequestService.get(headers);

        assertThat(itemRequestDtos, is(notNullValue()));
    }

    @Test
    void getRequestListOfOtherRequesters() throws Exception {
        User owner = User.builder()
                .id(1L)
                .name("name1")
                .email("user1@email.com")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(owner));

        LocalDateTime requestCreationDate = LocalDateTime.now();

        User requester = User.builder()
                .id(2L)
                .name("name2")
                .email("user2@email.com")
                .build();

        Request request = Request.builder()
                .id(1L)
                .description("description")
                .requester(requester)
                .created(requestCreationDate)
                .build();

        List<Request> itemRequests = new ArrayList<>();

        headers.put("x-sharer-user-id", "1");
        when(itemRequestRepository.findAllByRequesterIdIsNot(any(), any()))
                .thenReturn(itemRequests);
        List<RequestDto> itemRequestDtos = itemRequestService.get(headers, 0L, 10L);
        assertTrue(itemRequestDtos.isEmpty());

        itemRequests = List.of(request);
        when(itemRequestRepository.findAllByRequesterIdIsNot(any(), any()))
                .thenReturn(itemRequests);

        List<Item> items = Collections.emptyList();
        when(itemRepository.findAllByRequestIdIn(List.of(1L)))
                .thenReturn(items);

        itemRequestDtos = itemRequestService.get(headers, 0L, 10L);
        assertTrue(itemRequestDtos.get(0).getItems().isEmpty());

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .request(request)
                .build();
        items = List.of(item);

        when(itemRepository.findAllByRequestIdIn(List.of(1L)))
                .thenReturn(items);

        itemRequestDtos = itemRequestService.get(headers, 0L, 10L);
        assertThat(itemRequestDtos, is(notNullValue()));
    }

    @Test
    void throwPaginationException() {
        User owner = User.builder()
                .id(1L)
                .name("name1")
                .email("user1@email.com")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(owner));

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemRequestService.get(headers, -1L, 10L));

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemRequestService.get(headers, 0L, 0L));
    }

    @Test
    void getRequestByIdByAnyUser() throws Exception {
        User owner = User.builder()
                .id(1L)
                .name("name1")
                .email("user1@email.com")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(owner));

        LocalDateTime requestCreationDate = LocalDateTime.now();

        Request request = Request.builder()
                .id(1L)
                .description("description")
                .requester(owner)
                .created(requestCreationDate)
                .build();

        when(itemRequestRepository.findItemRequestById(request.getId()))
                .thenReturn(Optional.of(request));

        List<Item> items = Collections.emptyList();
        when(itemRepository.findAllByRequestId(1L))
                .thenReturn(items);

        headers.put("x-sharer-user-id", "1");
        RequestDto itemRequestDto = itemRequestService.get(headers, 1L);
        assertTrue(itemRequestDto.getItems().isEmpty());

        Item item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .request(request)
                .build();
        items = List.of(item);

        when(itemRepository.findAllByRequestId(1L))
                .thenReturn(items);

        itemRequestDto = itemRequestService.get(headers, 1L);

        assertThat(itemRequestDto, is(notNullValue()));
    }

    @Test
    void throwItemRequestNotFoundException() {
        User owner = User.builder()
                .id(1L)
                .name("name1")
                .email("user1@email.com")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(owner));

        when(itemRequestRepository.findItemRequestById(any()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundValueException.class,
                () -> itemRequestService.get(headers, 1L));
    }
}
