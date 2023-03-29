package ru.practicum.shareit.UserTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {
    @InjectMocks
    UserServiceImpl service;

    @Mock
    UserRepository repository;

    User newUser;
    Item item;


    @BeforeEach
    void init() {
        item = new Item();
        item.setId(1);
        item.setOwnerId(1);
        item.setAvailable(false);

        newUser = new User();
        newUser.setEmail("test@ema.il");
        newUser.setName("Testy Test");
        newUser.setItems(Set.of(item));
    }

    @Test
    void createUser_whenAddUser_thenReturnUserWithId() {
        when(repository.save(any(User.class)))
                .thenReturn(newUser);

        User actualUser = service.addUser(new User());

        assertEquals(newUser.getId(), actualUser.getId());
    }


    @Test
    void getUserById_And_ExceptionWhenNotFound() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(newUser));
        when(repository.findById(-1L))
                .thenThrow(new ElementNotFoundException("user is not found"));

        User actualUser = service.getUserById(1);

        assertEquals(newUser, actualUser);
        assertThrows(ElementNotFoundException.class, () -> service.getUserById(-1L));
    }

    @Test
    void changeUser_And_ExceptionWhenNotFound() {
        when(repository.getById(1L))
                .thenReturn(newUser);
        when(repository.getById(-1L))
                .thenThrow(new ElementNotFoundException("user is not found"));

        User changedUser = new User();
        changedUser.setName("ChangedUser");
        changedUser.setId(1);

        service.changeUser(changedUser);
        assertEquals(newUser.getName(), changedUser.getName());

        changedUser.setId(-1L);
        assertThrows(ElementNotFoundException.class, () -> service.changeUser(changedUser));
    }

    @Test
    void getAll() {
        when(repository.findAll())
                .thenReturn(List.of(new User()));

        List<User> users = service.getAll();
        assertEquals(users, List.of(new User()));
    }
}
