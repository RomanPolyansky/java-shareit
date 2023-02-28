package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static Item mapToItem(AddItemRequest dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setAvailable(dto.getAvailable());
        item.setDescription(dto.getDescription());
        return item;
    }

    public static Item mapToItem(UpdateItemRequest dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setAvailable(dto.getAvailable());
        item.setDescription(dto.getDescription());
        return item;
    }
    
    public static ItemResponse mapItemToResponse(Item item) {
        ItemResponse itemDto = new ItemResponse();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }
}