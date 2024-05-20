package ru.practicum.shareit.heandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.heandler.exception.BadRequestException;
import ru.practicum.shareit.heandler.exception.InternalServerErrorException;
import ru.practicum.shareit.heandler.exception.NotFoundException;
import ru.practicum.shareit.heandler.exception.NotFoundValueException;

import javax.validation.ValidationException;
import java.util.Map;

@RestControllerAdvice("ru.practicum.shareit")
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class, BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest() {
        return Map.of("Validation error", "Check your request.");
    }

    @ExceptionHandler({NullPointerException.class, NotFoundValueException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundRequest() {
        return Map.of("Validation error", "Check your request.");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleServerError() {
        return Map.of("Server error", "We are fixing the problem. The service will be up and running soon.");
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleInternalServerErrorException(final InternalServerErrorException e) {
        return new ResponseEntity<>(
                Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
