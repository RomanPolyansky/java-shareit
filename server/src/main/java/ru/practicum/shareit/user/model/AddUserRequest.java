package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class AddUserRequest {
    @NotBlank(message = "'name' should not be blank")
    String name;
    @NotBlank(message = "'email' should not be blank")
    @Email(message = "'email' should be email-like")
    String email;
}