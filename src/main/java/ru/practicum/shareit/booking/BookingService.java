package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

public interface BookingService {
    Booking addBooking(Booking booking);

    Booking replyBooking(long bookingId, String isApproved, long requesterId);

    Booking getBookingById(long bookingId, long requesterId);

    Iterable<Booking> getAllBookingsOfUser(long bookerId, String state);

    Iterable<Booking> getAllBookingsOfOwnerItems(long ownerId, String state);

    Booking getNextBooking(long itemId);

    Booking getLastBooking(long itemId);
}
