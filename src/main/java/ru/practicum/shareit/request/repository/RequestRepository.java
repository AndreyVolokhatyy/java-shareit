package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterIdOrderByCreatedDesc(Long userId);

    Optional<Request> findItemRequestById(Long id);

    List<Request> findAllByRequesterIdIsNot(Long userId, PageRequest pageRequest);
}
