package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.dto.constraints.Create;
import ru.practicum.shareit.item.dto.constraints.Update;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    private final BookingService bookingService;

    @PostMapping
    public ItemDto addItem(@RequestBody @Validated(Create.class) ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") long ownerId) {
        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwnerId(ownerId);
        log.info("Received POST ItemDto {} from {}", itemDto, ownerId);
        return ItemMapper.mapItemToResponse(service.addItem(item));
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(
            @PathVariable("id") long id,
            @RequestBody @Validated(Update.class) ItemDto itemDto,
            @NotBlank @RequestHeader("X-Sharer-User-Id") long ownerId) {
        Item item = ItemMapper.mapToItem(itemDto);
        item.setId(id);
        item.setOwnerId(ownerId);
        log.info("Received PATCH ItemDto {} from {}", itemDto, ownerId);
        return ItemMapper.mapItemToResponse(service.changeItem(item));
    }

    @GetMapping("/{id}")
    public ItemDto getItem(
            @PathVariable("id") long id,
            @NotBlank @RequestHeader("X-Sharer-User-Id") long requesterId) {
        log.info("Received GET id {} from {}", id, requesterId);
        Item item = service.getItemById(id);
        ItemDto itemDto = ItemMapper.mapItemToResponse(item);

        List<Comment> comments = service.fetchComments(item.getId());
        List<CommentDto> commentsDto = comments.stream()
                .map(CommentMapper::mapCommentToResponse)
                .collect(Collectors.toList());
        itemDto.setComments(commentsDto);

        if (item.getOwnerId() == requesterId) {
            BookingShortDto nextBooking = bookingService.getNextBooking(item.getId());
            BookingShortDto lastBooking = bookingService.getLastBooking(item.getId());
            itemDto.setNextBooking(nextBooking);
            itemDto.setLastBooking(lastBooking);
        }

        return itemDto;
    }

    @GetMapping()
    public List<ItemDto> getAllItemsOfOwner(
            @NotBlank @RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("Received GET id all from {}", ownerId);
        return service.getItemsByOwnerId(ownerId).stream()
                .map(ItemMapper::mapItemToResponse)
                .peek(itemDto -> {
                    itemDto.setNextBooking(bookingService.getNextBooking(itemDto.getId()));
                    itemDto.setLastBooking(bookingService.getLastBooking(itemDto.getId()));
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchByName(
            @RequestParam (name = "text") String text,
            @RequestHeader("X-Sharer-User-Id") @NotBlank long ownerId) {
        log.info("Received GET search of {} from {}", text, ownerId);
        return service.searchForItemsByText(text).stream()
                .map(ItemMapper::mapItemToResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/comment")
    public CommentDto addComment(@RequestBody @Validated(Create.class) CommentDto commentDto,
                              @PathVariable("id") long itemId,
                              @RequestHeader("X-Sharer-User-Id") long commentatorId) {
        Comment comment = CommentMapper.mapToComment(commentDto, itemId, commentatorId);
        log.info("Received POST CommentDto {} from {}", commentDto, commentatorId);
        return CommentMapper.mapCommentToResponse(service.addComment(comment));
    }
}
