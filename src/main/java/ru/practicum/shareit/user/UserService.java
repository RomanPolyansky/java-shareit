package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;


interface UserService {
    User getUserById(long id);
    List<User> getAll();
    User addUser(User user);
    User changeUser(User user);
    void deleteUser(long id);
}
