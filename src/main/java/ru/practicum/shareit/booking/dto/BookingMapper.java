package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    public static Booking mapToBooking(BookingDto dto) {
        Booking booking = new Booking();
        booking.setStartDate(dto.getStart());
        booking.setEndDate(dto.getEnd());
        booking.setId(dto.getId());
        return booking;
    }

    public static BookingDtoResponse mapBookingToResponse(Booking booking) {
        BookingDtoResponse bookingDto = new BookingDtoResponse();
        bookingDto.setItem(booking.getItemId());
        bookingDto.setStart(booking.getStartDate());
        bookingDto.setEnd(booking.getEndDate());
        bookingDto.setId(booking.getId());
        bookingDto.setBookerId(booking.getBookerId());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }
}