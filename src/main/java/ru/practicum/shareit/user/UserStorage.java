package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> getUserById(long id);
    List<User> getAll();
    User addUser(User user);
    User changeUser(User user);
    void deleteUser(long id);
    
}
