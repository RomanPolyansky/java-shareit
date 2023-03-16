package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

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
    public BookingDto addBooking(@RequestBody @Validated(Create.class) BookingDto bookingDto,
                                 @RequestHeader("X-Sharer-User-Id") long ownerId)  {
        Booking booking = BookingMapper.mapToBooking(bookingDto);
        booking.setStatus(Status.WAITING);
        booking.setBookerId(ownerId);
        log.info("Received POST BookingDto {} from {}", bookingDto, ownerId);
        return BookingMapper.mapBookingToResponse(service.addBooking(booking));
    }

    @PatchMapping
    public BookingDto replyBooking(@RequestParam (name = "isApproved") String isApproved,
                                   @NotBlank @PathVariable("bookingId") long bookingId,
                                   @NotBlank @RequestHeader("X-Sharer-User-Id") long requesterId) {
        log.info("Received Patch reply BookingDto {} from {}", requesterId);
        return BookingMapper.mapBookingToResponse(service.replyBooking(bookingId, isApproved, requesterId));
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(
            @PathVariable("bookingId") long bookingId,
            @NotBlank @RequestHeader("X-Sharer-User-Id") long requesterId) {
        log.info("Received GET booking {} from {}", bookingId, requesterId);
        return BookingMapper.mapBookingToResponse(service.getBookingById(bookingId, requesterId));
    }

    @GetMapping()
    public List<BookingDto> getAllBookingsOfUser(
            @NotBlank @RequestHeader("X-Sharer-User-Id") long bookerId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        log.info("Received GET all from bookerId {} with state {}", bookerId, state);
        return service.getAllBookingsOfUser(bookerId, state).stream()
                .map(BookingMapper::mapBookingToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsOfUserItems(
            @NotBlank @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        log.info("Received GET all from ownerId {} with state {}", ownerId, state);
        return service.getAllBookingsOfUserItems(ownerId, state).stream()
                .map(BookingMapper::mapBookingToResponse)
                .collect(Collectors.toList());
    }
}
