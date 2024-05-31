package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Autowired
    private JacksonTester<Booking> jsonBooking;

    @Test
    void testBookingDto() throws Exception {
        BookingDto bookingDto = BookingDto
                .builder()
                .id(1L)
                .start(LocalDateTime.of(2022, 10, 12, 12, 17, 0))
                .end(LocalDateTime.of(2022, 10, 12, 12, 18, 0))
                .itemId(1L)
                .bookerId(1L)
                .status(Status.WAITING)
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.itemId");
        assertThat(result).hasJsonPath("$.bookerId");
        assertThat(result).hasJsonPath("$.status");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-10-12T12:17:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-10-12T12:18:00");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }


    @Test
    void testBookingDtoLine() throws Exception {
        BookingDto bookingDto = BookingDto
                .builder()
                .id(1L)
                .start(LocalDateTime.of(2022, 10, 12, 12, 17, 0))
                .end(LocalDateTime.of(2022, 10, 12, 12, 18, 0))
                .itemId(1L)
                .bookerId(1L)
                .status(Status.WAITING)
                .build();

        JsonContent<Booking> result = jsonBooking.write(BookingDto.toBooking(bookingDto));

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).hasJsonPath("$.status");

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-10-12T12:17:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-10-12T12:18:00");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }

    @Test
    void testBookingNullPointerException() {
        Assertions.assertThrows(NullPointerException.class,
                () -> BookingDto.toBookingDto(Booking.builder()
                        .id(1L)
                        .start(LocalDateTime.now())
                        .end(LocalDateTime.now())
                        .item(null)
                        .booker(null)
                        .status(Status.WAITING)
                        .build()));
    }
}