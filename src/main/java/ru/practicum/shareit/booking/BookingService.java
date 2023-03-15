package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(Booking booking);

    Booking replyBooking(Booking booking, String isApproved);

    Booking getBookingById(long bookingId, long ownerId);

    List<Booking> getBookingsOfOwner(long ownerId, long state);
}
