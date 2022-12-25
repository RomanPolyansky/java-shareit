package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
public class AddItemRequest {
    @NotBlank(message = "'name' should not be blank")
    String name;
    @NotBlank(message = "'description' should not be blank")
    String description;
    @NotBlank
    boolean available;
}
