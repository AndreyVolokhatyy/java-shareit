package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.storage.CommentStorage;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentStorage commentStorage;

    @Autowired
    private MockMvc mvc;

    private ItemDto itemDtoCreateTest;

    private ItemCreatedDto itemDtoCreated;

    private ItemDto itemDtoUpdateTest;

    private ItemDto itemDto;

    private ItemCreatedDto itemDtoUpdated;

    private CommentDto commentDtoCreateTest;

    private CommentDto commentDtoCreated;

    private Map<String, String> headers;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("user@email.com")
                .build();

        itemDtoCreateTest = ItemDto.builder()
                .name("nameCreate")
                .description("create description")
                .available(true)
                .owner(1)
                .build();

        itemDtoCreated = ItemCreatedDto.builder()
                .id(1L)
                .name("nameCreate")
                .description("create description")
                .available(true)
                .user(user)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("nameCreate")
                .description("create description")
                .available(true)
                .owner(1)
                .build();

        itemDtoUpdateTest = ItemDto.builder()
                .description("update description")
                .build();

        itemDtoUpdated = ItemCreatedDto.builder()
                .id(1L)
                .name("nameCreate")
                .description("update description")
                .available(true)
                .user(user)
                .build();

        commentDtoCreateTest = CommentDto.builder()
                .text("comment")
                .build();

        commentDtoCreated = CommentDto.builder()
                .id(1L)
                .text("comment")
                .authorName("nameCreate")
                .created(LocalDateTime.now())
                .build();


        headers = new HashMap<>();
        headers.put("x-sharer-user-id", "1");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("Accept", "application/json");
        headers.put("Content-Length", "164");
    }

    @AfterEach
    void tearDown() {
        itemDtoCreateTest = null;
        itemDtoCreated = null;
        itemDtoUpdateTest = null;
        itemDtoUpdated = null;
    }

    @Test
    void create() throws Exception {
        when(itemService.createItem(headers, itemDtoCreateTest))
                .thenReturn(itemDtoCreated);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoCreateTest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("x-sharer-user-id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemDtoCreated.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoCreated.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoCreated.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoCreated.getAvailable())))
                .andExpect(jsonPath("$.user", is(itemDtoCreated.getUser()), User.class));
    }

    @Test
    void update() throws Exception {
        headers.put("Content-Length", "156");
        when(itemService.updateItem(headers, 1L, itemDtoUpdateTest))
                .thenReturn(itemDtoUpdated);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDtoUpdateTest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("x-sharer-user-id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoUpdated.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoUpdated.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoUpdated.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoUpdated.getAvailable())))
                .andExpect(jsonPath("$.user", is(itemDtoCreated.getUser()), User.class));
    }

    @Test
    void getItemDto() throws Exception {
        headers.remove("Content-Type", "application/json;charset=UTF-8");
        headers.remove("Content-Length", "164");

        when(itemService.getItemDto(headers, 1L))
                .thenReturn(itemDto);

        mvc.perform(get("/items/1")
                        .header("x-sharer-user-id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void getAll() throws Exception {
        headers.remove("Content-Type", "application/json;charset=UTF-8");
        headers.remove("Content-Length", "164");
        when(itemService.getItemUser(headers))
                .thenReturn(Set.of(itemDto));

        mvc.perform(get("/items/")
                        .header("x-sharer-user-id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));
    }

    @Test
    void search() throws Exception {
        headers.remove("Content-Type", "application/json;charset=UTF-8");
        headers.remove("Content-Length", "164");
        when(itemService.searchItem("update"))
                .thenReturn(Set.of(itemDtoCreated));

        mvc.perform(get("/items/search?text=update")
                        .header("x-sharer-user-id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoCreated.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoCreated.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoCreated.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDtoCreated.getAvailable())));
    }

    @Test
    void comment() throws Exception {
        headers.put("Content-Length", "61");
        when(itemService.addComment(commentDtoCreateTest, 1L, headers))
                .thenReturn(commentDtoCreated);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDtoCreateTest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("x-sharer-user-id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDtoCreated.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDtoCreated.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDtoCreated.getAuthorName())))
                .andExpect(jsonPath("$.created",
                        is(commentDtoCreated.getCreated().format(DateTimeFormatter.ISO_DATE_TIME))));
    }
}