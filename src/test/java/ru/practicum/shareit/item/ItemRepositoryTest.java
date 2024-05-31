package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.repository.ItemStorage;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.repository.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ItemStorage itemStorage;

    @Test
    @DirtiesContext
    void searchAvailableByText() {
        User owner = User.builder()
                .name("user1")
                .email("user1@email.com")
                .build();

        owner = userStorage.save(owner);

        Item item = Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .build();

        item = itemStorage.save(item);

        List<Item> items = itemStorage.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue("name", "descr");
        Assertions.assertTrue(items.get(0).getName().contains(item.getName()));
    }

    @Test
    @DirtiesContext
    void searchFindAllByRequestId() {
        User owner = User.builder()
                .name("user1")
                .email("user1@email.com")
                .build();

        User owner2 = User.builder()
                .name("user2")
                .email("user2@email.com")
                .build();

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .description("asd")
                .requester(owner2)
                .build();

        owner = userStorage.save(owner);
        userStorage.save(owner2);
        requestRepository.save(request);

        Item item = Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .request(request)
                .build();

        item = itemStorage.save(item);

        List<Item> items = itemStorage.findAllByRequestId(1L);
        Assertions.assertTrue(items.get(0).getName().contains(item.getName()));
    }

    @Test
    @DirtiesContext
    void searchFindAllByRequestIdIn() {
        User owner = User.builder()
                .name("user1")
                .email("user1@email.com")
                .build();

        User owner2 = User.builder()
                .name("user2")
                .email("user2@email.com")
                .build();

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .description("asd")
                .requester(owner2)
                .build();

        owner = userStorage.save(owner);
        userStorage.save(owner2);
        requestRepository.save(request);

        Item item = Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .user(owner)
                .request(request)
                .build();

        item = itemStorage.save(item);

        List<Item> items = itemStorage.findAllByRequestIdIn(List.of(1L));
        Assertions.assertTrue(items.get(0).getName().contains(item.getName()));
    }
}