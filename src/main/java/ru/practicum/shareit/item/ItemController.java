package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.AddItemRequest;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @PostMapping
    public ItemResponse addItem(@RequestBody @Valid AddItemRequest itemDto,
                                @RequestHeader("X-Sharer-User-Id") long ownerId) {
        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwnerId(ownerId);
        log.info("Received POST AddItemRequest {} from {}", itemDto, ownerId);
        return ItemMapper.mapItemToResponse(service.addItem(item));
    }

    @PatchMapping("/{id}")
    public ItemResponse updateItem(
            @PathVariable("id") long id,
            @RequestBody @Valid UpdateItemRequest itemDto,
            @NotBlank @RequestHeader("X-Sharer-User-Id") long ownerId) {
        Item item = ItemMapper.mapToItem(itemDto);
        item.setId(id);
        item.setOwnerId(ownerId);
        log.info("Received PATCH UpdateItemRequest {} from {}", itemDto, ownerId);
        return ItemMapper.mapItemToResponse(service.changeItem(item));
    }

    @GetMapping("/{id}")
    public ItemResponse getItem(
            @PathVariable("id") long id,
            @NotBlank @RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("Received GET id {} from {}", id, ownerId);
        return ItemMapper.mapItemToResponse(service.getItemById(id));
    }

    @GetMapping()
    public List<ItemResponse> getAllItemsOfOwner(
            @NotBlank @RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("Received GET id all from {}", ownerId);
        return service.getItemsByOwnerId(ownerId).stream()
                .map(ItemMapper::mapItemToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemResponse> searchByName(
            @RequestParam (name = "text") String text,
            @RequestHeader("X-Sharer-User-Id") @NotBlank long ownerId) {
        log.info("Received GET search of {} from {}", text, ownerId);
        return service.searchForItemsByText(text).stream()
                .map(ItemMapper::mapItemToResponse)
                .collect(Collectors.toList());
    }
}