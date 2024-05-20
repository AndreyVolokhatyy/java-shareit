package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User creatUser(UserDto userDto);

    User updateUser(long id, UserDto userDto);

    User getUser(long id);

    void deleteUser(long id);

    List<User> getUsers();
}
