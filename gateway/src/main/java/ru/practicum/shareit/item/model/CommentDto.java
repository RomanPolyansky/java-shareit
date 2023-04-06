package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.constraint.Create;

import javax.validation.constraints.NotBlank;

@Data
public class CommentDto {

    @NotBlank(groups = {Create.class}, message = "'text' should not be blank")
    private String text;

}
