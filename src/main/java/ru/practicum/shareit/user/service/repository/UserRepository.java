package ru.practicum.shareit.user.service.repository;

import ru.practicum.shareit.user.model.User;

import java.util.stream.Stream;

public interface UserRepository {

    User get(long id);

    void put(User user);

    void delete(long id);

    Stream<User> getStream();
}
