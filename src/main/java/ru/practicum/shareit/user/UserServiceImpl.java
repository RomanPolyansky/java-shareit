package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public boolean isValidUser(User receivedUser) {
        for (User user : userStorage.getAll()) {
            if (user.getEmail().equals(receivedUser.getEmail())) return false;
        }
        return true;
    }

    public User getUserById(long id) {
        Optional<User> optionalUser = userStorage.getUserById(id);
        return optionalUser.orElseThrow(
                () -> new NoSuchElementException("user is not found")
        );
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User addUser(User user) {
        if (!isValidUser(user)) throw new NoSuchElementException("user to add is not valid");
        return userStorage.addUser(user);
    }

    public User changeUser(User user) {
        if (!exists(user)) throw new NoSuchElementException("user to change is not found");
        if (!isValidUser(user)) throw new NoSuchElementException("user to change is not valid");
        return userStorage.changeUser(user);
    }

    public void deleteUser(long id) {
        if (!exists(id)) throw new NoSuchElementException("user to delete is not found");
        userStorage.deleteUser(id);
    }

    public boolean exists(long id) {
        return userStorage.getUserById(id).isPresent();
    }

    public boolean exists(User user) {
        return userStorage.getUserById(user.getId()) != null;
    }
}