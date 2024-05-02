package ru.practicum.shareit.user.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.repository.UserRepository;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceInMemory implements UserService {

    private UserRepository userRepository;
    private static int id = 1;

    public UserServiceInMemory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User creatUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getName() == null) {
            throw new ValidationException();
        }
        User user = UserDto.toUser(userDto);
        if (checkEmail(user.getEmail()) != 0) {
            throw new RuntimeException();
        }
        user.setId(id);
        userRepository.put(user);
        id++;
        return user;
    }

    @Override
    public User updateUser(int id, UserDto userDto) {
        User user = userRepository.get(id);
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
        return user;
    }

    @Override
    public User getUser(int id) {
        return userRepository.get(id);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.delete(id);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getStream().collect(Collectors.toList());
    }

    private int checkEmail(String email) {
        return userRepository.getStream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst().map(User::getId).orElse(0);
    }
}
