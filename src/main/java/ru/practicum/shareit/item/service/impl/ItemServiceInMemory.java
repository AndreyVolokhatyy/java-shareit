package ru.practicum.shareit.item.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.heandler.exception.NotFoundValueException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.repository.ItemRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceInMemory implements ItemService {

    private ItemRepository itemRepository;
    private static int id = 1;
    private ItemRequestService itemRequestService;
    private UserService userService;

    public ItemServiceInMemory(ItemRequestService itemRequestService, UserService userService, ItemRepository itemRepository) {
        this.itemRequestService = itemRequestService;
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    @Override
    public Item createItem(Map<String, String> headers, ItemDto itemDto) {
        int userId = getUserId(headers);
        if (!checkUser(userId)) {
            throw new NotFoundValueException();
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
        itemRepository.put(item);
        id++;
        return item;
    }

    @Override
    public Item updateItem(Map<String, String> headers, int itemId, ItemDto itemDto) {
        Item item = itemRepository.get(itemId);
        int userId = getUserId(headers);
        if (!checkUser(userId) || item.getOwner() != userId) {
            throw new NotFoundValueException();
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
    public Item getItem(Map<String, String> headers, int id) {
        Item item = itemRepository.get(id);
        int userId = getUserId(headers);
        if (!checkUser(userId)) {
            throw new NotFoundValueException();
        }
        return item;
    }

    @Override
    public Set<Item> getItemUser(Map<String, String> headers) {
        int userId = getUserId(headers);
        return itemRepository.getStream()
                .filter(i -> i.getOwner() == userId)
                .collect(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(Item::getId)))
                );
    }

    @Override
    public Set<Item> searchItem( String text) {
        if (text.isEmpty()) {
            return new HashSet<>();
        }
        return itemRepository.getStream()
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

    private int getUserId(Map<String, String> headers) {
            return Integer.parseInt(headers.get("x-sharer-user-id"));
    }
}
