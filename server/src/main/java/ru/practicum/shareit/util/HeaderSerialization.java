package ru.practicum.shareit.util;

import java.util.Map;

public class HeaderSerialization {

    public static Long getUserId(Map<String, String> headers) {
        if (headers != null) {
            if (headers.get("x-sharer-user-id") != null && !headers.get("x-sharer-user-id").isEmpty()) {
                return Long.parseLong(headers.get("x-sharer-user-id"));
            }
        }
        return null;
    }
}
