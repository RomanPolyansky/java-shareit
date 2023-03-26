package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

public class ItemMapper {

    public static Item mapToItem(ItemDto dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setAvailable(dto.getAvailable());
        item.setDescription(dto.getDescription());
        if (dto.getRequestId() != null) item.setItemRequest(setItemRequestById(dto.getRequestId()));
        return item;
    }
    
    public static ItemDto mapItemToResponse(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if (item.getItemRequest() != null) itemDto.setRequestId(item.getItemRequest().getId());
        return itemDto;
    }

    private static ItemRequest setItemRequestById(long iterRequestId) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(iterRequestId);
        return itemRequest;
    }
}