package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {

    private int id;
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
