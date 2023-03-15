package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;

    @Override
    public Booking addBooking(Booking booking) {
        if (!isValidBooking(booking)) throw new NoSuchElementException("booking to add is not valid");
        return repository.save(booking);
    }

    @Override
    public Booking replyBooking(Booking booking, String isApproved) {
        Status status = getStatusFromReply(isApproved);
        if (!isValidBooking(booking)) throw new NoSuchElementException("booking to add is not valid");
        if (!isOwnerOfBooking(booking)) throw new NoSuchElementException("is not owner of booking to reply");

        return null;
    }

    @Override
    public Booking getBookingById(long bookingId, long ownerId) {

        return null;
    }

    @Override
    public List<Booking> getBookingsOfOwner(long ownerId, long state) {
        return null;
    }

    private Status getStatusFromReply(String isApproved) {
        if (isApproved.toLowerCase().equals("true")) {
            return Status.APPROVED;
        } else if (isApproved.toLowerCase().equals("false")) {
            return Status.REJECTED;
        } else {
            throw new IllegalArgumentException("illegal status provided");
        }
    }

    private boolean isValidBooking(Booking booking) {
        return true;//TODO
    }

    private boolean isOwnerOfBooking(Booking booking) {
        return true;//TODO
    }
}
