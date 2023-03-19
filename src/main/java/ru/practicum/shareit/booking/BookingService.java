package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingService {
    Booking addBooking(Booking booking);

    Booking replyBooking(long bookingId, String isApproved, long requesterId);

    Booking getBookingById(long bookingId, long requesterId);

    List<Booking> getAllBookingsOfUser(long bookerId, String state);

    List<Booking> getAllBookingsOfUserItems(long ownerId, String state);
}
