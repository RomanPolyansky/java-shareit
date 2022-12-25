package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
public class UpdateUserRequest {
    String name;
    String email;
}
