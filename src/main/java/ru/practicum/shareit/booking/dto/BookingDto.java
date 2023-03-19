package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.dto.constraints.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDto {

    @NotNull(groups = {Create.class}, message = "'itemId' should not be null")
    private long itemId;

    private long bookerId;

    @DateTimeFormat(pattern = "yyyy-MM-ddThh:mm:ss")
    @NotBlank(groups = {Create.class}, message = "'start' should not be null")
    private LocalDateTime start;

    @DateTimeFormat(pattern = "yyyy-MM-ddThh:mm:ss")
    @NotBlank(groups = {Create.class}, message = "'end' should not be null")
    private LocalDateTime  end;
}
