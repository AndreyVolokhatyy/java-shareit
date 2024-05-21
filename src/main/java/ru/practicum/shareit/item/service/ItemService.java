package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Map;
import java.util.Set;

public interface ItemService {

    ItemCreatedDto createItem(Map<String, String> headers, ItemDto itemDto);

    ItemCreatedDto updateItem(Map<String, String> headers, long itemId, ItemDto itemDto);

    Item getItem(Map<String, String> headers, long id);

    ItemDto getItemDto(Map<String, String> headers, long id);

    Set<ItemDto> getItemUser(Map<String, String> headers);

    Set<ItemCreatedDto> searchItem(String text);

    CommentDto addComment(CommentDto commentCreatedDto, long itemId, Map<String, String> headers);
}
