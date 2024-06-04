package ru.practicum.shareit.user.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.heandler.exception.NotFoundValueException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.repository.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto creatUser(UserDto userDto) {
        User user = UserDto.toUser(userDto);
        return UserDto.toUserDto(userStorage.save(user));
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        User user = userStorage.findById(id).orElseThrow(NotFoundValueException::new);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (checkEmail(userDto.getEmail()) == 0 || checkEmail(userDto.getEmail()) == id) {
                user.setEmail(userDto.getEmail());
            } else {
                throw new RuntimeException("Error add email. Change value");
            }
        }
        return UserDto.toUserDto(userStorage.save(user));
    }

    @Override
    public UserDto getUser(long id) {
        return UserDto.toUserDto(get(id));
    }

    @Override
    public void deleteUser(long id) {
        userStorage.deleteById(id);
    }

    @Override
    public List<UserDto> getUsers() {
        return userStorage.findAll().stream().map(UserDto::toUserDto).collect(Collectors.toList());
    }

    @Override
    public User get(long id) {
        return userStorage.findById(id).orElseThrow(NotFoundValueException::new);
    }

    private long checkEmail(String email) {
        return userStorage.findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst().map(User::getId).orElse(0L);
    }
}
