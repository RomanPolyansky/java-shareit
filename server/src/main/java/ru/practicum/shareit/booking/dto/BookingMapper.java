package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking mapToBooking(BookingDto dto) {
        Booking booking = new Booking();
        booking.setStartDate(dto.getStart());
        booking.setEndDate(dto.getEnd());
        User booker = new User();
        booker.setId(dto.getBookerId());
        booking.setBooker(booker);
        Item item = new Item();
        item.setId(dto.getItemId());
        booking.setItem(item);
        return booking;
    }

    public static BookingDtoResponse mapBookingToResponse(Booking booking) {
        BookingDtoResponse bookingDto = new BookingDtoResponse();
        bookingDto.setStart(booking.getStartDate());
        bookingDto.setEnd(booking.getEndDate());
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setItem(booking.getItem());
        bookingDto.setBooker(booking.getBooker());
        return bookingDto;
    }
}