package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

public interface RequestStorage extends JpaRepository<Request, Long> {
}
