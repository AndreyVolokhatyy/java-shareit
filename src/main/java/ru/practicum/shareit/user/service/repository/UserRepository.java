package ru.practicum.shareit.user.service.repository;

import ru.practicum.shareit.user.model.User;

import java.util.stream.Stream;

public interface UserRepository {

    User get(int id);

    void put(User user);

    void delete(int id);

    Stream<User> getStream();
}
