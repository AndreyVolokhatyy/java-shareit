package ru.practicum.shareit.heandler.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String m) {
        super(m);
    }
}
