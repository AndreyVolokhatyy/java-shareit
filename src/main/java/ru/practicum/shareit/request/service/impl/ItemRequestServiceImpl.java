package ru.practicum.shareit.request.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private static Map<Integer, ItemRequest> itemRequests = new HashMap<>();
    private static int id = 1;

    @Override
    public Map<Integer, ItemRequest> getItemRequests() {
        return new HashMap<>(itemRequests);
    }
}
