package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface BookingService {
    Booking addBooking(Booking booking);

    Booking replyBooking(long bookingId, String isApproved, long requesterId);

    Booking getBookingById(long bookingId, long requesterId);

    List<Booking> getAllBookingsOfUser(long requesterId, String state,
                                       @PositiveOrZero int from, @Positive int size);

    List<Booking> getAllBookingsOfOwnerItems(long ownerId, String state,
                                             @PositiveOrZero int from, @Positive int size);
}
