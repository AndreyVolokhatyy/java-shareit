package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.user.model.User;

@JsonTest
class UserTest {

    @Test
    void testEquals() {
        User user1 = User.builder()
                .id(1L)
                .build();

        User user2 = User.builder()
                .id(1L)
                .build();

        User user3 = User.builder()
                .id(null)
                .build();

        Assertions.assertTrue(user1.equals(user2));
        Assertions.assertTrue(user1.equals(user1));
        Assertions.assertFalse(user1.equals(1L));
        Assertions.assertFalse(user1.equals(user3));
        Assertions.assertFalse(user3.equals(user1));
    }
}