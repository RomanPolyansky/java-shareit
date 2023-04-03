package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.configuration.PagesConfig;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private ItemRequestService itemRequestService;
    private ItemService itemService;
    private static final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(HEADER) long requesterId,
                                            @RequestBody ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto);

        ItemRequestDto itemRequestDtoResponse = ItemRequestMapper
                .mapItemRequestToResponse(
                        itemRequestService.addItemRequest(requesterId, itemRequest));

        return itemRequestDtoResponse;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader(HEADER) long requesterId,
                                                   @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                                   @Positive @RequestParam(value = "size", defaultValue = PagesConfig.DEFAULT_SIZE_AS_STRING) int size)  {

        List<ItemRequestDto> itemRequestDtoResponse = itemRequestService.getAllItemRequests(requesterId, from, size)
                .stream()
                .map(ItemRequestMapper::mapItemRequestToResponse)
                .collect(Collectors.toList());

        return itemRequestDtoResponse;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader(HEADER) long requesterId, 
                                             @PathVariable long requestId) {

        ItemRequestDto itemRequestDtoResponse = ItemRequestMapper
                .mapItemRequestToResponse(
                        itemRequestService.getItemRequestById(requestId, requesterId));
        List<Item> items = itemService.getItemsByRequestId(requestId);

        itemRequestDtoResponse.setItems(items.stream()
                .map(ItemMapper::mapItemToResponse)
                .collect(Collectors.toList()));
        if (itemRequestDtoResponse.getItems() == null)
            itemRequestDtoResponse.setItems(Collections.emptyList());
        
        return itemRequestDtoResponse;
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestByRequester(@RequestHeader(HEADER) long requesterId) {
        List<ItemRequestDto> itemRequestDtoResponse = itemRequestService.getItemRequestByRequester(requesterId)
                .stream()
                .peek(itemRequest -> itemRequest.setItems(itemService.getItemsByRequestId(itemRequest.getId())))
                .map(ItemRequestMapper::mapItemRequestToResponse)
                .collect(Collectors.toList());

        itemRequestDtoResponse.forEach(itemRequestDto -> {
                    if (itemRequestDto.getItems() == null) itemRequestDto.setItems(Collections.emptyList());
                });

        return itemRequestDtoResponse;
    }
}
