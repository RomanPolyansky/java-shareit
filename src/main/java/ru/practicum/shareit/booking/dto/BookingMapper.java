package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;

public class BookingMapper {
    public static Booking mapToBooking(BookingDto dto) {
        Booking booking = new Booking();
        booking.setItemId(dto.getItemId());
        booking.setStartDate(dto.getStart());
        booking.setEndDate(dto.getEnd());
        return booking;
    }

    public static BookingDto mapBookingToResponse(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(booking.getItemId());
        bookingDto.setStart(booking.getStartDate());
        bookingDto.setEnd(booking.getEndDate());
        return bookingDto;
    }
}
