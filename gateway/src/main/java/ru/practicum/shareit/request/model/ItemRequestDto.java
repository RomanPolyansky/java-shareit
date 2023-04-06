package ru.practicum.shareit.request.model;

import javax.validation.constraints.NotBlank;

public class ItemRequestDto {

    @NotBlank(message = "Description cannot be blank")
    private String description;
}
