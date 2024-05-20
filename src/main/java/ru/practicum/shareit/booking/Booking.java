package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Booking {

    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int idItem;
    private int booker;
    private String status;
}
