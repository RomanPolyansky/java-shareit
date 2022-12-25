package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemResponse {
    String name;
    String description;
    boolean isAvailable;
}
