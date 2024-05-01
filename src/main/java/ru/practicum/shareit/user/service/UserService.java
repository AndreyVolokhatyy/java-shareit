package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User creatUser(UserDto userDto);

    User updateUser(int id, UserDto userDto);

    User getUser(int id);

    void deleteUser(int id);

    List<User> getUsers();
}
