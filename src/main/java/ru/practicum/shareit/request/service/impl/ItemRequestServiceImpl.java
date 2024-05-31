package ru.practicum.shareit.request.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemRequestServiceImpl implements RequestService {

    private static Map<Integer, Request> itemRequests = new HashMap<>();
    private static int id = 1;

    @Override
    public Map<Integer, Request> getItemRequests() {
        return new HashMap<>(itemRequests);
    }
}
