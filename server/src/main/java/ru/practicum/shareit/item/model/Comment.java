package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // уникальный идентификатор комментария;
    private String text;            // содержимое комментария;
    @ManyToOne()
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;              // вещь, к которой относится комментарий;
    @ManyToOne()
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;            // автор комментария;
    private LocalDateTime created;  // дата создания комментария.
}
