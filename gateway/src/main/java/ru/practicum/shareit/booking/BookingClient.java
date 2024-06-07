package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(Map<String, String> headers, BookingState state, Integer from, Integer size) {
        String path = "?state=" + state.name() + "&from=" + from;
        if (size != null && size < 0 || from < 0) {
            throw new IllegalArgumentException("Значение должно быть больше или равно 0");
        }

        if (size != null) {
            path += "&size=" + size;
        }
        return get(path, headers, null);
    }

    public ResponseEntity<Object> getBookingsOwner(Map<String, String> headers, BookingState state, Integer from, Integer size) {
        String path = "/owner?state=" + state.name() + "&from=" + from;
        if (size != null && size < 0 || from < 0) {
            throw new IllegalArgumentException("Значение должно быть больше или равно 0");
        }

        if (size != null) {
            path += "&size=" + size;
        }

        return get(path, headers, null);
    }


    public ResponseEntity<Object> create(Map<String, String> headers, BookItemRequestDto requestDto) {
        if (requestDto.getStart() == null
                || requestDto.getEnd() == null) {
            throw new IllegalArgumentException("Неверная дата бронировния");
        }
        if (requestDto.getEnd().isBefore(requestDto.getStart())
                || requestDto.getStart().equals(requestDto.getEnd())) {
            throw new IllegalArgumentException("Неверная дата бронировния");
        }
        return post("", headers, requestDto);
    }

    public ResponseEntity<Object> getBooking(Map<String, String> headers, Long bookingId) {
        return get("/" + bookingId, headers);
    }

    public ResponseEntity<Object> update(Long bookingId, Map<String, String> headers, Boolean approved) {
        String path = "/" + bookingId + "?approved=" + approved;
        return patch(path, headers, null, null);
    }
}
