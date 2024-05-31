package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    List<Booking> findByItemIdAndBookerId(long itemId, long bookerId);

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId, Pageable pageable);

    List<Booking> findAllByEndBeforeAndBookerIdOrderByStartDesc(LocalDateTime localDateTime, long bookerId, Pageable pageable);

    List<Booking> findAllByStartAfterAndBookerIdOrderByStartDesc(LocalDateTime localDateTime, long bookerId, Pageable pageable);

    List<Booking> findAllByEndAfterAndStartBeforeAndBookerIdOrderByStartDesc(LocalDateTime before, LocalDateTime after, long bookerId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, Status status, Pageable pageable);

    //ItemOwner
    List<Booking> findAllByItemUserIdOrderByStartDesc(long userId, Pageable pageable);

    List<Booking> findByEndBeforeAndItemUserIdOrderByStartDesc(LocalDateTime now, long userId, Pageable pageable);

    List<Booking> findAllByItemUserIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByEndAfterAndStartBeforeAndItemUserIdOrderByStartDesc(LocalDateTime before, LocalDateTime after, long userId, Pageable pageable);

    List<Booking> findAllByEndAfterAndStartBeforeAndItemUserIdOrderByStartDesc(LocalDateTime before, LocalDateTime after, long userId);

    List<Booking> findAllByItemUserIdAndStatusOrderByStartDesc(long userId, Status status, Pageable pageable);

    //For ItemService
    List<Booking> findAllByItemIdAndEndBeforeOrderByEndDesc(long itemId, LocalDateTime localDateTime);

    List<Booking> findAllByItemIdAndStartAfter(long itemId, LocalDateTime localDateTime);
}
