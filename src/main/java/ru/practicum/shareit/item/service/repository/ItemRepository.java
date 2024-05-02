package ru.practicum.shareit.item.service.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.stream.Stream;

public interface ItemRepository {

    Item get(int id);

    void put(Item item);

    Stream<Item> getStream();
}
