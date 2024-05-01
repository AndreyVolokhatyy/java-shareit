package ru.practicum.shareit.item.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceInMemory implements ItemService {

    private static Map<Integer, Item> items = new HashMap<>();
    private static int id = 1;
    private ItemRequestService itemRequestService;
    private UserService userService;

    public ItemServiceInMemory(ItemRequestService itemRequestService, UserService userService) {
        this.itemRequestService = itemRequestService;
        this.userService = userService;
    }

    @Override
    public Item createItem(int userId, ItemDto itemDto) {
        if (!checkUser(userId)) {
            throw new NullPointerException();
        }
        if (itemDto.getAvailable() == null
                || itemDto.getName() == null
                || itemDto.getName().isEmpty()
                || itemDto.getDescription() == null
        ) {
            throw new ValidationException();
        }
        Item item = ItemDto.toItem(itemDto);
        item.setId(id);
        item.setOwner(userId);
        item.setRequest(itemRequestService.getItemRequests().get(itemDto.getRequest()));
        items.put(id, item);
        id++;
        return item;
    }

    @Override
    public Item updateItem(int userId, int itemId, ItemDto itemDto) {
        Item item = items.get(itemId);
        if (!checkUser(userId) || item.getOwner() != userId) {
            throw new NullPointerException();
        }
        if (itemDto.getName() != null && !itemDto.getName().isEmpty()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getRequest() != null) {
            item.setRequest(itemRequestService.getItemRequests().get(itemDto.getRequest()));
        }
        return item;
    }

    @Override
    public Item getItem(int userId, int id) {
        Item item = items.get(id);
        if (!checkUser(userId)) {
            throw new NullPointerException();
        }
        return item;
    }

    @Override
    public Set<Item> getItemUser(int userId) {
        return items.values().stream()
                .filter(i -> i.getOwner() == userId)
                .collect(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(Item::getId)))
                );
    }

    @Override
    public Set<Item> searchItem(int userId, String text) {
        if (text.isEmpty()) {
            return new HashSet<>();
        }
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(Item::getId)))
                );
    }

    private boolean checkUser(int id) {
        boolean check = false;
        for (User user : userService.getUsers()) {
            if (user.getId() == id) {
                check = true;
                break;
            }
        }
        return check;
    }
}
