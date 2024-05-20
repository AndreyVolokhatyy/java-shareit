package ru.practicum.shareit.item.service.repository.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.repository.ItemRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    private static Map<Long, Item> items = new HashMap<>();

    @Override
    public Item get(int id) {
        return items.get(id);
    }

    @Override
    public void put(Item item) {
        items.put(item.getId(), item);
    }

    @Override
    public Stream<Item> getStream() {
        return new ArrayList<>(items.values()).stream();
    }
}
