package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final RequestClient client;

    @Autowired
    public ItemRequestController(RequestClient requestClient) {
        this.client = requestClient;
    }

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestBody @Valid ItemRequestDto requestDto) {
        return client.addItemRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return client.getItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return client.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable("requestId") Long id) {
        return client.getItemRequestById(userId, id);
    }
}
