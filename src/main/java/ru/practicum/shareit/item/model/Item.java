package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Item {
    long id;
    long ownerId;
    String name;
    String description;
    Boolean available;

    public Item() {
    }
}
