package ru.practicum.shareit.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.State;

class ConverterTest {

    @Test
    void testEquals() {
        StringToEnumConverter converter = new StringToEnumConverter();
        Assertions.assertEquals(converter.convert("aLl"), State.ALL);
        Assertions.assertThrows(IllegalArgumentException.class, () -> converter.convert("bitc"));
    }
}