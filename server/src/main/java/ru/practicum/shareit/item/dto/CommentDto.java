package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.constraints.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    @NotBlank(groups = {Create.class}, message = "'text' should not be blank")
    private String text;
    private long id;
    private String authorName;
    private LocalDateTime created;

}
