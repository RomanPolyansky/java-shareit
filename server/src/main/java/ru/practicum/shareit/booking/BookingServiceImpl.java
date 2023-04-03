package ru.practicum.shareit.booking;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.exception.IlligalRequestException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Booking addBooking(Booking booking) {
        if (!isValidNewBooking(booking)) throw new ElementNotFoundException("booking to add is not valid");
        Booking fullBooking = getFullBooking(booking);
        if (!fullBooking.getItem().getAvailable()) {
            throw new IlligalRequestException("item not available");
        }
        if (fullBooking.getItem().getOwnerId() == booking.getBooker().getId()) {
            throw new ElementNotFoundException("Cannot book owned item");
        }
        return bookingRepository.save(fullBooking);
    }

    @Override
    public Booking replyBooking(long bookingId, String isApproved, long requesterId) {
        boolean toApprove;
        if (isApproved.equalsIgnoreCase("true")) {
            toApprove = true;
        } else if (isApproved.equalsIgnoreCase("false")) {
            toApprove = false;
        } else {
            throw new IllegalArgumentException("illegal status provided");
        }

        Booking booking = bookingRepository.getById(bookingId);
        if (!isValidBookingReply(booking)) throw new ElementNotFoundException("booking to reply is not valid");
        if (!isOwnerOfItem(booking, requesterId))
            throw new ElementNotFoundException("is not owner of booking to reply");
        if (toApprove && booking.getStatus().equals(BookingStatus.APPROVED))
            throw new IlligalRequestException("Can not approve already approved");

        if (toApprove) {
            booking.setStatus(BookingStatus.APPROVED);
            booking.setStatusStr(BookingStatus.APPROVED.toString());
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            booking.setStatusStr(BookingStatus.REJECTED.toString());
        }

        return bookingRepository.save(getFullBooking(booking));
    }

    @Override
    public Booking getBookingById(long bookingId, long requesterId) {
        Booking bookingFromRepo = getFullBooking(bookingId);
        if (bookingFromRepo.getBooker().getId() == requesterId) {
            return bookingFromRepo;
        } else if (isOwnerOfItem(bookingFromRepo, requesterId)) {
            return bookingFromRepo;
        } else {
            throw new ElementNotFoundException("No access to this booking");
        }
    }

    @Override
    public List<Booking> getAllBookingsOfUser(long requesterId, String state,
                                              @PositiveOrZero int from, @Positive int size) {
        userService.getUserById(requesterId); //throws exception if not found
        BooleanExpression eqStatus = getStatusFilterFromStateStr(state);
        BooleanExpression eqOwner = QBooking.booking.booker.id.eq(requesterId);

        List<Booking> foundBookings = jpaQueryFactory.selectFrom(QBooking.booking)
                .where(eqStatus)
                .where(eqOwner)
                .orderBy(QBooking.booking.startDate.desc())
                .offset(from)
                .limit(size)
                .fetch();

        foundBookings.forEach(this::getFullBooking);

        return foundBookings;
    }

    @Override
    public List<Booking> getAllBookingsOfOwnerItems(long ownerId, String state,
                                                    @PositiveOrZero int from, @Positive int size) {
        userService.getUserById(ownerId); // throws exception if not exist
        BooleanExpression eqStatus = getStatusFilterFromStateStr(state);
        BooleanExpression eqOwner = QBooking.booking.item.ownerId.eq(ownerId);

        List<Booking> foundBookings = jpaQueryFactory.selectFrom(QBooking.booking)
                .where(eqStatus)
                .where(eqOwner)
                .orderBy(QBooking.booking.startDate.desc())
                .offset(from)
                .limit(size)
                .fetch();

        foundBookings.forEach(this::getFullBooking);

        return foundBookings;
    }

    public BookingShortDto getNextBooking(long itemId) {
        Booking booking = jpaQueryFactory.selectFrom(QBooking.booking)
                .where(QBooking.booking.item.id.eq(itemId))
                .where(QBooking.booking.statusStr.eq(BookingStatus.APPROVED.toString()))
                .where(QBooking.booking.startDate.after(LocalDateTime.now()))
                .orderBy(QBooking.booking.startDate.asc())
                .fetchFirst();
        return booking == null ? null : new BookingShortDto(booking.getId(), booking.getBooker().getId());
    }

    public BookingShortDto getLastBooking(long itemId) {
        Booking booking = jpaQueryFactory.selectFrom(QBooking.booking)
                .where(QBooking.booking.item.id.eq(itemId))
                .where(QBooking.booking.statusStr.eq(BookingStatus.APPROVED.toString()))
                .where(QBooking.booking.startDate.before(LocalDateTime.now()))
                .orderBy(QBooking.booking.startDate.desc())
                .fetchFirst();
        return booking == null ? null : new BookingShortDto(booking.getId(), booking.getBooker().getId());
    }

    private boolean isValidNewBooking(Booking booking) {
        if (booking.getStartDate() == null || booking.getEndDate() == null) {
            throw new IlligalRequestException("No start date or end date");
        }
        if (booking.getStartDate().isBefore(LocalDateTime.now()) ||
                booking.getEndDate().isBefore(LocalDateTime.now())) {
            throw new IlligalRequestException("start or end before now");
        }
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new IlligalRequestException("start is after end");
        }
        if (booking.getStartDate().isEqual(booking.getEndDate())) {
            throw new IlligalRequestException("start is equal end");
        }
        return true;
    }

    private boolean isValidBookingReply(Booking booking) {
        if (booking.getEndDate().isBefore(LocalDateTime.now())) {
            throw new IlligalRequestException("end before now");
        }
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new IlligalRequestException("start is after end");
        }
        if (booking.getStartDate().isEqual(booking.getEndDate())) {
            throw new IlligalRequestException("start is equal end");
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

    private BookingState getStatusFromState(String state) {
        try {
            return BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Unknown state: " + state);
        }
    }

    private Booking getFullBooking(long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new ElementNotFoundException("Booking with this id not found");
        }
        Booking booking = optionalBooking.get();
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

    private BooleanExpression getStatusFilterFromStateStr(String stateString) {
        BookingState state = getStatusFromState(stateString);
        BooleanExpression eqStatus;
        switch (state) {
            case ALL:
                eqStatus = Expressions.asBoolean(true).isTrue();
                break;
            case CURRENT:
                eqStatus = Expressions.asDateTime(LocalDateTime.now())
                        .between(QBooking.booking.startDate, QBooking.booking.endDate);
                break;
            case PAST:
                eqStatus = QBooking.booking.endDate.before(LocalDateTime.now());
                break;
            case FUTURE:
                eqStatus = QBooking.booking.startDate.after(LocalDateTime.now());
                break;
            case REJECTED:
            case WAITING:
            case APPROVED:
                eqStatus = QBooking.booking.statusStr.eq(state.toString());
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return eqStatus;
    }
}
