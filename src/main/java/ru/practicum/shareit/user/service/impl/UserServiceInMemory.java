package ru.practicum.shareit.user.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceInMemory implements UserService {

    private static Map<Integer, User> users = new HashMap<>();
    private static int id = 1;

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
        users.put(id, user);
        id++;
        return user;
    }

    @Override
    public User updateUser(int id, UserDto userDto) {
        User user = users.get(id);
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
        return users.get(id);
    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private int checkEmail(String email) {
        int id = 0;
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                id = user.getId();
                break;
            }
        }
        return id;
    }
}
