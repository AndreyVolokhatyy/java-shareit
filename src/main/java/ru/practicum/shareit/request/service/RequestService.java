package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.Request;

import java.util.Map;

public interface RequestService {

    Map<Integer, Request> getItemRequests();
}
