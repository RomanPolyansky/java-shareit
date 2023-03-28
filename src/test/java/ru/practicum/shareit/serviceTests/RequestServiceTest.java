package ru.practicum.shareit.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.exception.IlligalRequestException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @InjectMocks
    private ItemRequestService service;
    @Mock
    private ItemRequestRepository repository;
    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    User newUser;
    Item itemWithId;
    Booking booking;

    ItemRequest itemRequest;

    @Captor
    private ArgumentCaptor<ItemRequest> captor;

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

        itemRequest = new ItemRequest();
        itemRequest.setRequester(newUser);
        itemRequest.setItems(List.of(itemWithId));
        itemRequest.setId(1L);
        itemRequest.setDescription("test");
        itemRequest.setCreated(LocalDateTime.now());

    }

    @Test
    void getRequset_And_Exception() {
        doReturn(newUser)
                .when(userService).getUserById(anyLong());
        doReturn(Optional.of(itemRequest))
                .when(repository).findById(1L);
        doThrow(ElementNotFoundException.class)
                .when(repository).findById(-1L);

        ItemRequest ir = service.getItemRequestById(1, 1);

        assertEquals(ir.getId(), itemRequest.getId());

        assertThrows(ElementNotFoundException.class, () -> service.getItemRequestById(-1L, 1L));
    }

    @Test
    void addRequset() {
        doReturn(newUser)
                .when(userService).getUserById(anyLong());

        ItemRequest ir = service.addItemRequest(1, itemRequest);

        verify(repository).save(captor.capture());
        ItemRequest irBeforeSave = captor.getValue();

        assertEquals(irBeforeSave.getId(), itemRequest.getId());
        assertEquals(irBeforeSave.getRequester(), itemRequest.getRequester());
    }

    @Test
    void addRequset_Exception_blankdescription() {
        itemRequest.setDescription("");
        doReturn(newUser)
                .when(userService).getUserById(anyLong());

        assertThrows(IlligalRequestException.class, () -> service.addItemRequest(1, itemRequest));
    }

    @Test
    void addRequset_Exception_nullDescription() {
        itemRequest.setDescription(null);
        doReturn(newUser)
                .when(userService).getUserById(anyLong());

        assertThrows(IlligalRequestException.class, () -> service.addItemRequest(1, itemRequest));
    }
}
