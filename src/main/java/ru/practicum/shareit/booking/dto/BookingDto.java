package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.constraints.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDto {
    @NotNull(groups = {Create.class}, message = "'itemId' should not be null")
    private long itemId;
    @NotBlank(groups = {Create.class}, message = "'start' should not be null")
    private Instant start;
    @NotBlank(groups = {Create.class}, message = "'end' should not be null")
    private Instant end;
}
