package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateItemRequest {
    String name;
    String description;
    Boolean available;
}