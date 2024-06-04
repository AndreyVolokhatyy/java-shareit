package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.Request;

class RequestTest {

    @Test
    void testEquals() {
        Request itemRequest1 = Request.builder()
                .id(1L)
                .build();

        Request itemRequest2 = Request.builder()
                .id(1L)
                .build();

        Request itemRequest3 = Request.builder()
                .id(null)
                .build();

        Assertions.assertEquals(itemRequest1, itemRequest2);
        Assertions.assertEquals(itemRequest1, itemRequest1);
        Assertions.assertNotEquals(1L, itemRequest1);
        Assertions.assertNotEquals(itemRequest1, itemRequest3);
        Assertions.assertNotEquals(itemRequest3, itemRequest1);
    }
}