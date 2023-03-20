package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(Booking booking);

    Booking replyBooking(long bookingId, String isApproved, long requesterId);

    Booking getBookingById(long bookingId, long requesterId);

    Iterable<Booking> getAllBookingsOfUser(long bookerId, String state);

    List<Booking> getAllBookingsOfOwnerItems(long ownerId, String state);
}
