package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestDto create(@RequestHeader Map<String, String> headers,
                             @Valid @RequestBody RequestDto requestDto) {
        return requestService.create(headers, requestDto);
    }

    @GetMapping
    public List<RequestDto> get(@RequestHeader Map<String, String> headers) {
        return requestService.get(headers);
    }

    @GetMapping("/all")
    public List<RequestDto> get(
            @RequestHeader Map<String, String> headers,
            @RequestParam(defaultValue = "0") Long from,
            @RequestParam(defaultValue = "10") Long size
    ) {
        return requestService.get(headers, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestDto get(@RequestHeader Map<String, String> headers, @PathVariable Long requestId) {
        return requestService.get(headers, requestId);
    }

}
