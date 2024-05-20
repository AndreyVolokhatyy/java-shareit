package ru.practicum.shareit.user.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.heandler.exception.NotFoundValueException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.repository.UserStorage;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User creatUser(UserDto userDto) {
        User user = UserDto.toUser(userDto);
        userStorage.save(user);
        return user;
    }

    @Override
    public User updateUser(long id, UserDto userDto) {
        User user = userStorage.findById(id).get();
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (checkEmail(userDto.getEmail()) == 0 || checkEmail(userDto.getEmail()) == id) {
                user.setEmail(userDto.getEmail());
            } else {
                throw new RuntimeException();
            }
        }
        userStorage.save(user);
        return user;
    }

    @Override
    public User getUser(long id) {
        return userStorage.findById(id).orElseThrow(NotFoundValueException::new);
    }

    @Override
    public void deleteUser(long id) {
        userStorage.deleteById(id);
    }

    @Override
    public List<User> getUsers() {
        return userStorage.findAll();
    }

    private long checkEmail(String email) {
        return userStorage.findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst().map(User::getId).orElse(0L);
    }
}
