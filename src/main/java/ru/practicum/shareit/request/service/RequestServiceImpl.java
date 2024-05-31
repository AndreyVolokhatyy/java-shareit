package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.heandler.exception.NotFoundValueException;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.repository.ItemStorage;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.page.PageRequestManager;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.repository.UserStorage;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    @Transactional
    public RequestDto create(Map<String, String> headers, RequestDto requestDto) {
        User user = userStorage.findById(getUserId(headers))
                .orElseThrow(NotFoundValueException::new);
        requestDto.setCreated(LocalDateTime.now());
        Request request = RequestDto.toRequest(requestDto, user);
        return RequestDto.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> get(Map<String, String> headers) {

        long userId = getUserId(headers);
        userStorage.findById(userId)
                .orElseThrow(NotFoundValueException::new);
        List<Request> itemRequests = requestRepository.findAllByRequesterIdOrderByCreatedDesc(
                userId);
        if (itemRequests.isEmpty()) {
            return Collections.emptyList();
        }
        List<RequestDto> requestDtos = itemRequests.stream()
                .map(RequestDto::toRequestDto)
                .collect(Collectors.toList());

        List<Long> requestIdList = requestDtos.stream()
                .map(RequestDto::getId)
                .collect(Collectors.toList());
        List<Item> items = itemStorage.findAllByRequestIdIn(requestIdList);

        for (RequestDto requestDto : requestDtos) {
            List<Item> requestItems = items.stream()
                    .filter(item -> item.getRequest().getId().equals(requestDto.getId()))
                    .collect(Collectors.toList());
            if (!requestItems.isEmpty()) {
                List<ItemCreatedDto> itemDto = requestItems.stream()
                        .map(ItemCreatedDto::toItemDto)
                        .collect(Collectors.toList());
                requestDto.setItems(itemDto);
            } else {
                requestDto.setItems(Collections.emptyList());
            }
        }
        return requestDtos;
    }

    @Override
    public List<RequestDto> get(Map<String, String> headers, Long from, Long size) {

        long userId = getUserId(headers);
        userStorage.findById(userId)
                .orElseThrow(NotFoundValueException::new);
        PageRequest pageRequest = PageRequestManager.form(
                from.intValue(), size.intValue(), Sort.Direction.DESC, "created");
        List<Request> itemRequests = requestRepository.findAllByRequesterIdIsNot(userId,
                pageRequest);

        List<RequestDto> requestDtos = itemRequests.stream()
                .map(RequestDto::toRequestDto)
                .collect(Collectors.toList());

        List<Long> requestIdList = requestDtos.stream()
                .map(RequestDto::getId)
                .collect(Collectors.toList());
        List<Item> items = itemStorage.findAllByRequestIdIn(requestIdList);

        for (RequestDto requestDto : requestDtos) {
            List<Item> requestItems = items.stream()
                    .filter(item -> item.getRequest().getId().equals(requestDto.getId()))
                    .collect(Collectors.toList());
            if (!requestItems.isEmpty()) {
                List<ItemCreatedDto> itemDto = requestItems.stream()
                        .map(ItemCreatedDto::toItemDto)
                        .collect(Collectors.toList());
                requestDto.setItems(itemDto);
            } else {
                requestDto.setItems(Collections.emptyList());
            }
        }
        return requestDtos;
    }

    @Override
    public RequestDto get(Map<String, String> headers, Long requestId) {

        userStorage.findById(getUserId(headers))
                .orElseThrow(NotFoundValueException::new);
        Request request = requestRepository.findItemRequestById(requestId)
                .orElseThrow(NotFoundValueException::new);
        RequestDto requestDto = RequestDto.toRequestDto(request);

        List<Item> items = itemStorage.findAllByRequestId(requestDto.getId());
        if (!items.isEmpty()) {
            List<ItemCreatedDto> itemDto = items.stream()
                    .map(ItemCreatedDto::toItemDto)
                    .collect(Collectors.toList());
            requestDto.setItems(itemDto);
        } else {
            requestDto.setItems(Collections.emptyList());
        }
        return requestDto;
    }

    private long getUserId(Map<String, String> headers) {
        return Integer.parseInt(headers.get("x-sharer-user-id"));
    }
}
