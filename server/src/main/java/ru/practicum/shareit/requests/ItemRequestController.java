package ru.practicum.shareit.requests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService service;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.service = itemRequestService;
    }

    @ResponseBody
    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader Map<String, String> headers) {
        return service.create(itemRequestDto, headers);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable("requestId") Long itemRequestId, @RequestHeader Map<String, String> headers) {
        return service.getItemRequestById(itemRequestId, headers);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnItemRequests(@RequestHeader Map<String, String> headers) {
        return service.getOwnItemRequests(headers);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader Map<String, String> headers,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(required = false) Integer size) {
        return service.getAllItemRequests(headers, from, size);
    }
}
