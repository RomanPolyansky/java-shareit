package ru.practicum.shareit.user.model;

import lombok.Data;
import ru.practicum.shareit.constraint.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    @NotBlank(groups = {Create.class}, message = "'name' should not be blank")
    String name;
    @NotBlank(groups = {Create.class}, message = "'email' should not be blank")
    @Email(groups = {Create.class},message = "'email' should be email-like")
    String email;
}
