package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequest mapToItemRequest(ItemRequestDto dto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(dto.getDescription());
        if (dto.getRequester() != null) itemRequest.setRequester(new User(dto.getRequester().getId()));
        return itemRequest;
    }
    
    public static ItemRequestDto mapItemRequestToResponse(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setRequester(UserMapper.mapUserToResponse(itemRequest.getRequester()));
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setItems(itemRequest.getItems().stream()
                .map(ItemMapper::mapItemToResponse)
                .collect(Collectors.toList()));
        return itemRequestDto;
    }
}