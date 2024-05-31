package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.repository.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.repository.UserStorage;

import java.util.List;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private ItemStorage itemStorage;

    @Test
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
}