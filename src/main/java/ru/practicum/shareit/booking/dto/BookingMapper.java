package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking mapToBooking(BookingDto dto) {
        Booking booking = new Booking();
        booking.setStartDate(dto.getStart());
        booking.setEndDate(dto.getEnd());
        booking.setItemId(dto.getItemId());
        return booking;
    }

    public static BookingDtoResponse mapBookingToResponse(Booking booking, Item item, User booker) {
        BookingDtoResponse bookingDto = new BookingDtoResponse();
        bookingDto.setStart(booking.getStartDate());
        bookingDto.setEnd(booking.getEndDate());
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());

        bookingDto.setBooker(booker);
        bookingDto.setItem(item);
        return bookingDto;
    }
}