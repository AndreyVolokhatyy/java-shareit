package ru.practicum.shareit.heandler.exception;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String m) {
        super(m);
    }
}
