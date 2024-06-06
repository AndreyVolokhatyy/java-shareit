package ru.practicum.shareit.user.model;

import lombok.*;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // уникальный идентификатор пользователя
    private String name;    // имя или логин пользователя
    private String email;   // адрес электронной почты
}
