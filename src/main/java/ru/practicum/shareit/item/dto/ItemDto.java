package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

@Data
public class ItemDto {

    private String name;
    private String description;
    private Boolean available;
    private int owner;
    private Integer request;

    public ItemDto(String name, String description, Boolean available, Integer request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null);
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable());
    }
}
