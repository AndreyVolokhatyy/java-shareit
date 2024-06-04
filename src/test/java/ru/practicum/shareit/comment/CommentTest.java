package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.model.Comment;

class CommentTest {

    @Test
    void testEquals() {
        Comment comment1 = Comment.builder()
                .id(1L)
                .build();

        Comment comment2 = Comment.builder()
                .id(1L)
                .build();

        Comment comment3 = Comment.builder()
                .id(null)
                .build();

        Assertions.assertEquals(comment1, comment2);
        Assertions.assertEquals(comment1, comment1);
        Assertions.assertNotEquals(1L, comment1);
        Assertions.assertNotEquals(comment1, comment3);
        Assertions.assertNotEquals(comment3, comment1);
    }
}