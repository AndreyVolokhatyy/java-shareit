package ru.practicum.shareit.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.request.RequestController;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    RequestService requestService;

    @Autowired
    private MockMvc mvc;

    private RequestDto requestDtoCreateTest;

    private RequestDto requestDto;

    private ItemCreatedDto itemDto;

    private Map<String, String> headers;

    @BeforeEach
    void setUp() {
        itemDto = ItemCreatedDto.builder()
                .id(1L)
                .name("nameCreate")
                .description("create description")
                .available(true)
                .requestId(1L)
                .build();

        requestDtoCreateTest = RequestDto.builder()
                .description("need smth")
                .build();

        requestDto = RequestDto.builder()
                .id(1L)
                .description("need smth")
                .requester(2L)
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();

        headers = new HashMap<>();
        headers.put("x-sharer-user-id", "2");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("Accept", "application/json");
        headers.put("Content-Length", "82");
    }

    @AfterEach
    void tearDown() {
        requestDtoCreateTest = null;
        requestDto = null;
        itemDto = null;
    }

    @Test
    void create() throws Exception {
        when(requestService.create(headers, requestDtoCreateTest))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDtoCreateTest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-sharer-user-id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.requester", is(requestDto.getRequester()), Long.class))
                .andExpect(jsonPath("$.created",
                        is(requestDto.getCreated().format(DateTimeFormatter.ISO_DATE_TIME))));
    }

    @Test
    void getOwnRequest() throws Exception {
        requestDto.setItems(List.of(itemDto));

        when(requestService.get(any()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests")
                        .header("x-sharer-user-id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[0].requester", is(requestDto.getRequester()), Long.class))
                .andExpect(jsonPath("$[0].created",
                        is(requestDto.getCreated().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].items[0].id",
                        is(requestDto.getItems().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].name",
                        is(requestDto.getItems().get(0).getName())))
                .andExpect(jsonPath("$[0].items[0].description",
                        is(requestDto.getItems().get(0).getDescription())))
                .andExpect(jsonPath("$[0].items[0].available",
                        is(requestDto.getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$[0].items[0].requestId",
                        is(requestDto.getItems().get(0).getRequestId()), Long.class));
    }

    @Test
    void getAll() throws Exception {
        requestDto.setItems(List.of(itemDto));

        when(requestService.get(any(), any(Long.class), any(Long.class)))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests/all")
                        .header("x-sharer-user-id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[0].requester", is(requestDto.getRequester()), Long.class))
                .andExpect(jsonPath("$[0].created",
                        is(requestDto.getCreated().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].items[0].id",
                        is(requestDto.getItems().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].name",
                        is(requestDto.getItems().get(0).getName())))
                .andExpect(jsonPath("$[0].items[0].description",
                        is(requestDto.getItems().get(0).getDescription())))
                .andExpect(jsonPath("$[0].items[0].available",
                        is(requestDto.getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$[0].items[0].requestId",
                        is(requestDto.getItems().get(0).getRequestId()), Long.class));
    }

    @Test
    void getRequestById() throws Exception {
        requestDto.setItems(List.of(itemDto));

        when(requestService.get(any(), any(Long.class)))
                .thenReturn(requestDto);

        mvc.perform(get("/requests/1")
                        .header("x-sharer-user-id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.requester", is(requestDto.getRequester()), Long.class))
                .andExpect(jsonPath("$.created",
                        is(requestDto.getCreated().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.items[0].id",
                        is(requestDto.getItems().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.items[0].name",
                        is(requestDto.getItems().get(0).getName())))
                .andExpect(jsonPath("$.items[0].description",
                        is(requestDto.getItems().get(0).getDescription())))
                .andExpect(jsonPath("$.items[0].available",
                        is(requestDto.getItems().get(0).getAvailable())));
    }
}