package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddItemRequest {
    @NotBlank(message = "'name' should not be blank")
    String name;
    @NotBlank(message = "'description' should not be blank")
    String description;
    @NotNull(message = "'available' should not be null")
    Boolean available;
}
