package ru.practicum.shareit.requests.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_request ")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                  // уникальный идентификатор запроса
    private String description;       // текст запроса, содержащий описание требуемой вещи
    @ManyToOne()
    @JoinColumn(name = "requestor_id", referencedColumnName = "id")
    private User requestor;           // пользователь, создавший запрос
    private LocalDateTime created;    // дата и время создания запроса
}
