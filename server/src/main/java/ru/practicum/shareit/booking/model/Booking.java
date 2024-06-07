package ru.practicum.shareit.booking.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // уникальный идентификатор бронирования
    @Column(name = "start_date")
    private LocalDateTime start;    // дата начала бронирования
    @Column(name = "end_date")
    private LocalDateTime end;      // дата конца бронирования
    @ManyToOne()
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;            // вещь, которую пользователь бронирует
    @ManyToOne()
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    private User booker;          // пользователь, который осуществляет бронирование
    @Enumerated(EnumType.STRING)
    private Status status;          // статус бронирования
}
