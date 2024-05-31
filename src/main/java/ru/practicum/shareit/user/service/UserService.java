package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto creatUser(UserDto userDto);

    UserDto updateUser(long id, UserDto userDto);

    UserDto getUser(long id);

    void deleteUser(long id);

    List<UserDto> getUsers();

    User get(long id);
}
