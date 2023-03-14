package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingDto addBooking(@RequestBody @Validated() BookingDto bookingDto,
                                 @RequestHeader("X-Sharer-User-Id") long ownerId)  {
        Booking booking = BookingMapper.mapToBooking(bookingDto);
        booking.setStatus(Status.WAITING);
        booking.setBookerId(ownerId);
        log.info("Received POST BookingDto {} from {}", bookingDto, ownerId);
        return BookingMapper.mapBookingToResponse(service.addBooking(booking));
    }

}
