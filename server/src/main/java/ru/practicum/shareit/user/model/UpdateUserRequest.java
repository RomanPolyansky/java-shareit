package ru.practicum.shareit.user.model;

import lombok.Data;

@Data
public class UpdateUserRequest {
    String name;
    String email;
}
