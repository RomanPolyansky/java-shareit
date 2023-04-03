package ru.practicum.shareit.Item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.exception.NoAccessException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {

    @InjectMocks
    ItemServiceImpl service;

    @Mock
    ItemRepository repository;

    @Mock
    UserServiceImpl userService;

    User userWithId;
    Item itemWithId;
    Item itemWithId2;
    Item itemWithId3;



    @BeforeEach
    void setup() {
        userWithId = new User();
        userWithId.setId(6L);
        userWithId.setEmail("ol@ya.ru");
        userWithId.setName("Olesya");

        itemWithId = new Item();
        itemWithId.setId(2L);
        itemWithId.setName("name");
        itemWithId.setDescription("description");
        itemWithId.setOwnerId(6L);
        itemWithId.setAvailable(true);

        itemWithId2 = new Item();
        itemWithId2.setId(2L);
        itemWithId2.setName("name2");
        itemWithId2.setDescription("description2");
        itemWithId2.setAvailable(false);
        itemWithId2.setOwnerId(3L);

        itemWithId3 = new Item();
        itemWithId3.setId(2L);
        itemWithId3.setName("title");
        itemWithId3.setDescription("description");
        itemWithId3.setAvailable(true);
    }

    @Test
    void changeItem_And_OwnerShip() {
        doReturn(userWithId)
                .when(userService).getUserById(anyLong());
        doReturn(Optional.of(itemWithId))
                .when(repository).findById(anyLong());
        doReturn(itemWithId2)
                .when(repository).getById(anyLong());
        doReturn(itemWithId2)
                .when(repository).save(any(Item.class));

        Item updatedItem = service.changeItem(itemWithId2);

        assertEquals(itemWithId2.getName(), updatedItem.getName());
        assertEquals(itemWithId2.getDescription(), updatedItem.getDescription());
        assertEquals(3L, updatedItem.getOwnerId());
        assertEquals(itemWithId2.getAvailable(), updatedItem.getAvailable());

        assertThrows(NoAccessException.class, () -> service.changeItem(itemWithId));
    }

    @Test
    void getItem_And_Exception() {
        doReturn(Optional.of(itemWithId))
                .when(repository).findById(1L);
        doThrow(ElementNotFoundException.class)
                .when(repository).findById(-1L);

        Item result = service.getItemById(1L);
        assertEquals(itemWithId, result);
        assertThrows(ElementNotFoundException.class, () -> service.getItemById(-1L));
    }

    @Test
    void addItem() {
        doReturn(userWithId)
                .when(userService).getUserById(6L);
        doThrow(ElementNotFoundException.class)
                .when(userService).getUserById(-1L);
        doReturn(itemWithId)
                .when(repository).save(any());

        Item result = service.addItem(itemWithId);

        Item badItem = new Item();
        badItem.setOwnerId(-1);

        assertEquals(itemWithId, result);
        assertThrows(ElementNotFoundException.class, () -> service.addItem(badItem));
    }
}
