package ru.practicum.shareit.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    String name;
    String email;
}
