package ru.practicum.shareit.Booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    @InjectMocks
    private BookingServiceImpl service;
    @Mock
    private BookingRepository repository;
    @Mock
    private ItemServiceImpl itemService;
    @Mock
    private UserService userService;
    User newUser;
    Item itemWithId;
    Booking booking;
    @Captor
    private ArgumentCaptor<Booking> captor;

    @BeforeEach
    private void init() {
        newUser = new User();
        newUser.setEmail("test@ema.il");
        newUser.setName("Testy Test");
        newUser.setId(1L);


        itemWithId = new Item();
        itemWithId.setId(2L);
        itemWithId.setName("name");
        itemWithId.setDescription("description");
        itemWithId.setOwnerId(6L);
        itemWithId.setAvailable(true);

        booking = new Booking();
        booking.setStartDate(LocalDateTime.parse("2030-03-26T19:00:00.000000"));
        booking.setEndDate(LocalDateTime.parse("2030-03-29T19:00:00.000000"));
        booking.setBooker(newUser);
        booking.setItem(itemWithId);

    }

    @Test
    void addBooking_And_ValidationExceptions() {
        Booking badBooking = new Booking();

        doReturn(newUser)
                .when(userService).getUserById(anyLong());
        doReturn(itemWithId)
                .when(itemService).getItemById(anyLong());
        doReturn(booking)
                .when(repository).save(any());

        Booking addedBooking = service.addBooking(booking);

        assertEquals(addedBooking.getStartDate(), booking.getStartDate());

        assertThrows(Exception.class, () -> service.addBooking(badBooking));
    }

    @Test
    void replyBooking_And_ValidationExceptions() {
        doReturn(itemWithId)
                .when(itemService).getItemById(anyLong());

        booking.setStatusStr("true");
        doReturn(booking)
                .when(repository).getById(anyLong());
        service.replyBooking(1L, "true", 6);
        verify(repository).save(captor.capture());
        Booking bookingBeforeSave = captor.getValue();
        assertEquals(bookingBeforeSave.getStatus(), BookingStatus.APPROVED);

        assertThrows(IllegalArgumentException.class, () -> service.replyBooking(1,"not status",1));
    }

    @Test
    void replyBookingReject_And_ValidationExceptions() {
        doReturn(itemWithId)
                .when(itemService).getItemById(anyLong());

        booking.setStatusStr("false");
        doReturn(booking)
                .when(repository).getById(anyLong());
        service.replyBooking(1L, "true", 6);
        verify(repository).save(captor.capture());
        Booking bookingBeforeSave = captor.getValue();
        assertEquals(bookingBeforeSave.getStatus(), BookingStatus.APPROVED);

        assertThrows(IllegalArgumentException.class, () -> service.replyBooking(1,"not status",1));
    }

    @Test
    void getBooking_And_ValidationExceptions() {
        doReturn(Optional.of(booking))
                .when(repository).findById(anyLong());
        doReturn(newUser)
                .when(userService).getUserById(anyLong());
        doReturn(itemWithId)
                .when(itemService).getItemById(anyLong());

        Booking receivedBooking = service.getBookingById(1L,  6);
        assertEquals(receivedBooking.getId(), booking.getId());
        assertEquals(receivedBooking.getBooker().getId(), booking.getBooker().getId());
        assertEquals(receivedBooking.getStartDate(), booking.getStartDate());
        assertEquals(receivedBooking.getEndDate(), booking.getEndDate());
        assertEquals(receivedBooking.getItem().getId(), booking.getItem().getId());

        assertThrows(IllegalArgumentException.class, () -> service.replyBooking(1,"not status",1));
    }
}
