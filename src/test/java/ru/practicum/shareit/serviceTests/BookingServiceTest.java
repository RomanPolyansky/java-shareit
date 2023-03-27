package ru.practicum.shareit.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.exception.NoAccessException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @InjectMocks
    private BookingService service;

    @Mock
    private BookingRepository repository;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    User newUser;
    Item itemWithId;
    Booking booking;

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


}
