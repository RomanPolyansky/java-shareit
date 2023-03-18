package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.constraints.Create;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDtoResponse {
    private long id;

    @JsonProperty("booker.id")
    private long bookerId;
    @JsonProperty("item.id")
    private long item;

    @DateTimeFormat(pattern = "yyyy-MM-ddThh:mm:ss")
    private LocalDateTime start;

    @DateTimeFormat(pattern = "yyyy-MM-ddThh:mm:ss")
    private LocalDateTime  end;

    private Status status;
}
