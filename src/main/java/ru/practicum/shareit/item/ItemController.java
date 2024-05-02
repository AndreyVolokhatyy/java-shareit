package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Map;
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
    public Item createItem(@RequestHeader Map<String, String> headers, @Valid @RequestBody ItemDto itemDto) {
        return itemService.createItem(headers, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item itemUpdate(@RequestHeader Map<String, String> headers,
                           @PathVariable(value = "itemId") int itemId,
                           @Valid @RequestBody ItemDto itemDto) {
        return itemService.updateItem(headers, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item getItem(@RequestHeader Map<String, String> headers, @PathVariable(value = "itemId") int id) {
        return itemService.getItem(headers, id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<Item> getItem(@RequestHeader Map<String, String> headers) {
        return itemService.getItemUser(headers);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Set<Item> searchItem(@RequestParam String text) {
        return itemService.searchItem(text);
    }
}
