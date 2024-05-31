package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemCreatedDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@Builder
public class RequestDto {

    private Long id;
    @NotBlank
    private String description;
    private Long requester;
    private LocalDateTime created;
    private List<ItemCreatedDto> items;

    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .items(Collections.emptyList())
                .build();
    }

    public static Request toRequest(RequestDto requestDto, User user) {
        return Request.builder()
                .id(requestDto.getId())
                .description(requestDto.getDescription())
                .requester(user)
                .created(requestDto.getCreated())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        return true;
    }
}
