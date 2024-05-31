package ru.practicum.shareit.converter;

import ru.practicum.shareit.booking.enums.State;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, State> {

    @Override
    public State convert(String source) {
        try {
            return State.valueOf(source.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown state: " + source);
        }
    }
}
