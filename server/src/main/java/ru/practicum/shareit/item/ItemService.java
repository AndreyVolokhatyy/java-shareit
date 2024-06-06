package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemService {

    ItemDto getItemById(Long id, Map<String, String> headers);

    Item findItemById(Long id);

    ItemDto create(ItemDto itemDto, Map<String, String> headers);

    List<ItemDto> getItemsByOwner(Map<String, String> headers, Integer from, Integer size);

    List<ItemDto> getItemsByOwner(Long ownerId, Integer from, Integer size);

    void delete(Long itemId, Map<String, String> headers);

    List<ItemDto> getItemsBySearchQuery(String text, Integer from, Integer size);

    ItemDto update(ItemDto itemDto, Map<String, String> headers, Long itemId);

    CommentDto createComment(CommentDto commentDto, Long itemId, Map<String, String> headers);

    List<CommentDto> getCommentsByItemId(Long itemId);

    List<ItemDto> getItemsByRequestId(Long requestId);
}
