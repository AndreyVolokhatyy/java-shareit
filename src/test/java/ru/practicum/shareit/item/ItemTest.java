package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

class ItemTest {

    @Test
    void testEquals() {
        Item item1 = Item.builder()
                .id(1L)
                .build();

        Item item2 = Item.builder()
                .id(1L)
                .build();

        Item item3 = Item.builder()
                .build();

        Assertions.assertEquals(item1, item2);
        Assertions.assertEquals(item1, item1);
        Assertions.assertNotEquals(1L, item1);
        Assertions.assertNotEquals(item1, item3);
        Assertions.assertNotEquals(item3, item1);
    }
}