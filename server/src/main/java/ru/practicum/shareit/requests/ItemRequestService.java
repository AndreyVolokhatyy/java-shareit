package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;
import java.util.Map;

public interface ItemRequestService {

    ItemRequestDto create(ItemRequestDto itemRequestDto, Map<String, String> headers);

    ItemRequestDto getItemRequestById(Long itemRequestId, Map<String, String> headers);

    List<ItemRequestDto> getOwnItemRequests(Map<String, String> headers);

    List<ItemRequestDto> getAllItemRequests(Map<String, String> headers, Integer from, Integer size);
}
