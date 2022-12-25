package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.AddItemRequest;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.UserMapper;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    public ItemResponse addItem(@RequestBody @Valid AddItemRequest itemDto,
                                @RequestHeader("X-Sharer-User-Id") long ownerId) {
        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwnerId(ownerId);
        return ItemMapper.mapItemToResponse(service.addEntity(item));
    }

    @PatchMapping("/{id}")
    public ItemResponse updateItem(
            @PathVariable("id") long id,
            @RequestBody @Valid UpdateItemRequest itemDto,
            @NotBlank @RequestHeader("X-Sharer-User-Id") long ownerId) {
        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwnerId(ownerId);
        return ItemMapper.mapItemToResponse(service.changeEntity(item));
    }
}
