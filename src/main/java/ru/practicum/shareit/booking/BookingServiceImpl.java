package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final ItemRepository itemRepository;


    @Override
    public Booking addBooking(Booking booking) {
        log.info(booking.toString());
        if (!isValidBooking(booking)) throw new NoSuchElementException("booking to add is not valid");
        return repository.save(booking);
    }

    @Override
    public Booking replyBooking(long bookingId, String isApproved, long requesterId) {
        Booking booking = repository.getById(bookingId);
        if (!isValidBooking(booking)) throw new NoSuchElementException("booking to reply is not valid");
        if (!isOwnerOfItem(booking, requesterId)) throw new NoSuchElementException("is not owner of booking to reply");

        if (isApproved.toLowerCase().equals("true")) {
            booking.recalculateStatus();
        } else if (isApproved.toLowerCase().equals("false")) {
            booking.setStatus(Status.REJECTED);
        } else {
            throw new IllegalArgumentException("illegal status provided");
        }
        return repository.save(booking);
    }

    @Override
    public Booking getBookingById(long bookingId, long requesterId) {
        Booking bookingFromRepo = repository.getById(bookingId);
        if (bookingFromRepo.getBookerId() == requesterId) {
            return bookingFromRepo;
        } else if (isOwnerOfItem(bookingFromRepo, requesterId)) {
            return bookingFromRepo;
        } else {
            throw new NoSuchElementException("No access to this booking");
        }
    }

    @Override
    public List<Booking> getAllBookingsOfUser(long bookerId, String state) {
        if (state.toLowerCase().equals("all")) {
            return repository.getBookingsByBookerIdOrderByStartDateDesc(bookerId);
        }
        Status status = getStatusFromState(state);
        return repository.getBookingsByBookerIdAndStatusOrderByStartDateDesc(bookerId, state);
    }

    private Status getStatusFromState(String state) {
        try {
            return Status.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("wrong status provided");
        }
    }

    @Override
    public List<Booking> getAllBookingsOfUserItems(long ownerId, String state) {
        return null;
    }

    private boolean isValidBooking(Booking booking) {
        return true;//TODO
    }

    private boolean isOwnerOfItem(Booking booking, long requesterId) {
        Item item = itemRepository.getById(booking.getItemId());
        return item.getOwnerId() == requesterId;
    }
}
