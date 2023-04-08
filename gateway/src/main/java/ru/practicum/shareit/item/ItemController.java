package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constraint.Create;
import ru.practicum.shareit.constraint.Update;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemClient client;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.client = itemClient;
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestBody @Validated(Create.class) ItemDto itemDto) {
        return client.create(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable("id") long id,
                                         @RequestBody @Validated(Update.class) ItemDto itemDto) {
        return client.update(userId, id, itemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable("id") Long itemId) {
        return client.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestParam(name = "from", required = false) Integer from,
                                     @RequestParam(name = "size", required = false) Integer size) {

        return client.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByText(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(name = "text", defaultValue = "") String text,
                                                 @PositiveOrZero @RequestParam(name = "from", required = false) Integer from,
                                                 @Positive @RequestParam(name = "size", required = false) Integer size) {

        return client.getItemsByText(text, userId, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @RequestBody @Validated(Create.class) CommentDto commentDTO) {
        return client.createComment(userId, itemId, commentDTO);
    }
}
