package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.heandler.exception.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ErrorResponseTest {

    @Test
    void getPaginationException() {
        PaginationException ex = new PaginationException("test");
        assertEquals("test", ex.getMessage());
    }

    @Test
    void getBadRequestException() {
        BadRequestException ex = new BadRequestException("test");
        assertEquals("test", ex.getMessage());
    }

    @Test
    void getInternalServerErrorException() {
        InternalServerErrorException ex = new InternalServerErrorException("test");
        assertEquals("test", ex.getMessage());
    }

    @Test
    void getNotFoundException() {
        NotFoundException ex = new NotFoundException("test");
        assertEquals("test", ex.getMessage());
    }

    @Test
    void getNotFoundValueException() {
        NotFoundValueException ex = new NotFoundValueException();
        assertNull(ex.getMessage());
    }
}