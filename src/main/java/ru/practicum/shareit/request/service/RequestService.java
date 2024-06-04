package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;
import java.util.Map;

public interface RequestService {

    RequestDto create(Map<String, String> headers, RequestDto requestDto);

    List<RequestDto> get(Map<String, String> headers);

    List<RequestDto> get(Map<String, String> headers, Long from, Long size);

    RequestDto get(Map<String, String> headers, Long requestId);
}
