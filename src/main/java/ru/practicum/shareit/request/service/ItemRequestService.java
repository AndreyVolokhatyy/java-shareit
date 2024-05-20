package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.ItemRequest;

import java.util.Map;

public interface ItemRequestService {

    Map<Integer, ItemRequest> getItemRequests();
}
