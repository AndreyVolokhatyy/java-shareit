package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Email;

@Data
public class UserDto {

    private String name;
    @Email
    private String email;

    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getName(), user.getEmail());
    }

    public static User toUser(UserDto userDto) {
        return new User(userDto.getName(), userDto.getEmail());
    }
}
