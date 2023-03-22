package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.stream.Collectors;

public class ItemMapper {

    public static Item mapToItem(ItemDto dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setAvailable(dto.getAvailable());
        item.setDescription(dto.getDescription());
        return item;
    }
    
    public static ItemDto mapItemToResponse(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if (!item.getComments().isEmpty())
            itemDto.setComments(item.getComments()
                    .stream()
                    .map(CommentMapper::mapCommentToResponse)
                    .collect(Collectors.toList()));
        if (item.getNextBooking() != null )
            itemDto.setNextBooking(BookingMapper
                    .mapBookingToResponse(item.getNextBooking()));
        if (item.getLastBooking() != null )
            itemDto.setLastBooking(BookingMapper
                    .mapBookingToResponse(item.getLastBooking()));
        return itemDto;
    }
}