package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.constraint.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemDto {
    @NotBlank(groups = {Create.class}, message = "'name' should not be blank")
    private String name;
    @NotBlank(groups = {Create.class}, message = "'description' should not be blank")
    private String description;
    @NotNull(groups = {Create.class}, message = "'available' should not be null")
    private Boolean available;

}