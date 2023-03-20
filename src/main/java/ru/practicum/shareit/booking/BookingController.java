package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    public BookingDtoResponse addBooking(@RequestBody @Validated(Create.class) BookingDto bookingDto,
                                         @RequestHeader("X-Sharer-User-Id") long bookerId)  {
        bookingDto.setBookerId(bookerId);
        Booking booking = BookingMapper.mapToBooking(bookingDto);
        log.info("Received POST BookingDto {} from {}", bookingDto, bookerId);
        return BookingMapper.mapBookingToResponse(service.addBooking(booking));
    }

    @PatchMapping("/{id}")
    public BookingDtoResponse replyBooking(@RequestParam (name = "approved") String isApproved,
                                           @NotBlank @PathVariable("id") long bookingId,
                                           @NotBlank @RequestHeader("X-Sharer-User-Id") long requesterId) {
        log.info("Received Patch reply BookingDto {} from {}", requesterId);

        return BookingMapper.mapBookingToResponse(service.replyBooking(bookingId, isApproved, requesterId));
    }

    @GetMapping("/{id}")
    public BookingDtoResponse getBooking(
            @PathVariable("id") long bookingId,
            @NotBlank @RequestHeader("X-Sharer-User-Id") long requesterId) {
        log.info("Received GET booking {} from {}", bookingId, requesterId);
        return BookingMapper.mapBookingToResponse(service.getBookingById(bookingId, requesterId));
    }

    @GetMapping()
    public Queue<BookingDtoResponse> getAllBookingsOfUser(
            @RequestHeader("X-Sharer-User-Id") @NotBlank long bookerId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        log.info("Received GET all from bookerId {} with state {}", bookerId, state);
        Iterable<Booking> bookingList = service.getAllBookingsOfUser(bookerId, state);
        Queue<BookingDtoResponse> bookingDtoResponseList = new LinkedList<>();
        for (Booking booking : bookingList) {
            bookingDtoResponseList.offer(BookingMapper.mapBookingToResponse(booking));
        }
        return bookingDtoResponseList;
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getAllBookingsOfUserItems(
            @NotBlank @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        log.info("Received GET all from ownerId {} with state {}", ownerId, state);
        return service.getAllBookingsOfOwnerItems(ownerId, state).stream()
                .map(BookingMapper::mapBookingToResponse)
                .collect(Collectors.toList());
    }
}
