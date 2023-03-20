package ru.practicum.shareit.booking;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

//import practicum.shareit.booking.model.QBooking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final ItemService itemService;
    private final UserService userService;


    @Override
    public Booking addBooking(Booking booking) {
        if (!isValidNewBooking(booking)) throw new NoSuchElementException("booking to add is not valid");
        Booking fullBooking = getFullBooking(booking);
        if (!fullBooking.getItem().getAvailable()) {
            throw new NoSuchElementException("item not available");
        }
        return repository.save(fullBooking);
    }

    @Override
    public Booking replyBooking(long bookingId, String isApproved, long requesterId) {
        Booking booking = repository.getById(bookingId);
        if (!isValidBooking(booking)) throw new NoSuchElementException("booking to reply is not valid");
        if (!isOwnerOfItem(booking, requesterId)) throw new NoSuchElementException("is not owner of booking to reply");

        if (isApproved.equalsIgnoreCase("true")) {
            booking.setStatus(Status.APPROVED);
        } else if (isApproved.equalsIgnoreCase("false")) {
            booking.setStatus(Status.REJECTED);
        } else {
            throw new IllegalArgumentException("illegal status provided");
        }

        return repository.save(getFullBooking(booking));
    }

    @Override
    public Booking getBookingById(long bookingId, long requesterId) {
        Booking bookingFromRepo = getFullBooking(bookingId);
        if (bookingFromRepo.getBooker().getId() == requesterId) {
            return bookingFromRepo;
        } else if (isOwnerOfItem(bookingFromRepo, requesterId)) {
            return bookingFromRepo;
        } else {
            throw new NoSuchElementException("No access to this booking");
        }
    }

    @Override
    public Iterable<Booking> getAllBookingsOfUser(long requesterId, String state) {
        userService.getUserById(requesterId); //throws exception if not found

        Status status = getStatusFromState(state);

        BooleanExpression eqStatus;
        if (status.equals(Status.ALL)) {
            eqStatus = Expressions.asBoolean(true).isTrue();
        } else {
            eqStatus = QBooking.booking.statusStr.eq(status.name());
        }
        log.info(eqStatus.toString());
        log.info(status.toString());

        BooleanExpression eqOwner = QBooking.booking.booker.id.eq(requesterId);

        Iterable<Booking> foundBookings = repository.findAll(eqStatus.and(eqOwner),
                new QSort(QBooking.booking.startDate.asc()));

        foundBookings.forEach(this::getFullBooking);

        return foundBookings;
    }

    @Override
    public List<Booking> getAllBookingsOfOwnerItems(long ownerId, String state) {
        return null;
    }

    private boolean isValidNewBooking(Booking booking) {
        if (booking.getStartDate().isBefore(LocalDateTime.now()) ||
                booking.getEndDate().isBefore(LocalDateTime.now())) {
            throw new NoSuchElementException("start or end before now");
        }
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new NoSuchElementException("start is after end");
        }
        if (booking.getStartDate().isEqual(booking.getEndDate())) {
            throw new NoSuchElementException("start is equal end");
        }
        return true;
    }

    private boolean isValidBooking(Booking booking) {
        if (booking.getEndDate().isBefore(LocalDateTime.now())) {
            throw new NoSuchElementException("end before now");
        }
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new NoSuchElementException("start is after end");
        }
        if (booking.getStartDate().isEqual(booking.getEndDate())) {
            throw new NoSuchElementException("start is equal end");
        }
        return true;
    }

    private Booking getFullBooking(Booking booking) {
        User booker = userService.getUserById(booking.getBooker().getId());
        booking.setBooker(booker);
        Item item = itemService.getItemById(booking.getItem().getId());
        booking.setItem(item);
        return booking;
    }

    private Status getStatusFromState(String state) {
        try {
            return Status.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("wrong status provided");
        }
    }

    private Booking getFullBooking(long bookingId) {
        Booking booking = repository.getById(bookingId);
        User booker = userService.getUserById(booking.getBooker().getId());
        booking.setBooker(booker);
        Item item = itemService.getItemById(booking.getItem().getId());
        booking.setItem(item);
        return booking;
    }

    private boolean isOwnerOfItem(Booking booking, long requesterId) {
        Item item = itemService.getItemById(booking.getItem().getId());
        return item.getOwnerId() == requesterId;
    }
}
