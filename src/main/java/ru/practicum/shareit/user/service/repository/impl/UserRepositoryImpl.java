package ru.practicum.shareit.user.service.repository.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class UserRepositoryImpl implements UserRepository {

    private static Map<Long, User> users = new HashMap<>();

    @Override
    public User get(int id) {
        return users.get(id);
    }

    @Override
    public void put(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void delete(int id) {
        users.remove(id);
    }

    @Override
    public Stream<User> getStream() {
        return new ArrayList<>(users.values()).stream();
    }
}
