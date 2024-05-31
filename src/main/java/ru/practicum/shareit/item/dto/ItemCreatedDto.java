package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ItemCreatedDto {

    private long id;
    private String name;
    private String description;
    private Boolean available;
    private User user;
    private Long requestId;

    public static ItemCreatedDto toItemDto(Item item) {
        return ItemCreatedDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .user(item.getUser())
                .requestId(Optional.ofNullable(item.getRequest()).map(Request::getId).orElse(null))
                .build();
    }
}
