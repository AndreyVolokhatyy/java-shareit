package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader Map<String, String> headers) {
        return itemService.getItemById(itemId, headers);
    }

    @ResponseBody
    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader Map<String, String> headers) {
        return itemService.create(itemDto, headers);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader Map<String, String> headers,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(required = false) Integer size) {
        return itemService.getItemsByOwner(headers, from, size);
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                          @RequestHeader Map<String, String> headers) {
        return itemService.update(itemDto, headers, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Long itemId, @RequestHeader Map<String, String> headers) {
        itemService.delete(itemId, headers);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearchQuery(@RequestParam String text,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(required = false) Integer size) {
        return itemService.getItemsBySearchQuery(text, from, size);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto commentDto, @RequestHeader Map<String, String> headers,
                                    @PathVariable Long itemId) {
        return itemService.createComment(commentDto, itemId, headers);
    }
}
