package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Set;

public interface ItemService {

    Item createItem(int userId, ItemDto itemDto);

    Item updateItem(int userId, int itemId, ItemDto itemDto);

    Item getItem(int userId, int id);

    Set<Item> getItemUser(int userId);

    Set<Item> searchItem(int userId, String text);
}
