package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemResponse {
    long id;
    String name;
    String description;
    Boolean available;
}
