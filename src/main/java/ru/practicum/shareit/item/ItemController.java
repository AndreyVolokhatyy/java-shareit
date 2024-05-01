package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/items")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem(@RequestHeader("X-Sharer-User-Id") int userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item itemUpdate(@RequestHeader("X-Sharer-User-Id") int userId,
                           @PathVariable(value = "itemId") int itemId,
                           @Valid @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item getItem(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable(value = "itemId") int id) {
        return itemService.getItem(userId, id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<Item> getItem(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItemUser(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Set<Item> searchItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                @RequestParam String text) {
        return itemService.searchItem(userId, text);
    }
}
