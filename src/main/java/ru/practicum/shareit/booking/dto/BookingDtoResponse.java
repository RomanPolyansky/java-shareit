package ru.practicum.shareit.booking.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDtoResponse {
    private long id;

    @DateTimeFormat(pattern = "yyyy-MM-ddThh:mm:ss")
    private LocalDateTime start;

    @DateTimeFormat(pattern = "yyyy-MM-ddThh:mm:ss")
    private LocalDateTime  end;

    private Status status;

    private Item item;
    private User booker;

    public void setItemWithId(long itemId) {
        Item item = new Item();
        item.setId(itemId);
        this.item = item;
    }
}
