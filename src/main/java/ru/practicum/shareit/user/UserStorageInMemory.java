package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserStorageInMemory implements UserStorage {

    private final Map<Long, User> userMap = new HashMap<>();
    private long currentId = 1;

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User addUser(User user) {
        user.setId(currentId++);
        userMap.put(user.getId(), user);
        return userMap.get(user.getId());
    }

    @Override
    public User changeUser(User receivedUser) {
        User user = userMap.get(receivedUser.getId());
        if (receivedUser.getEmail() != null) user.setEmail(receivedUser.getEmail());
        if (receivedUser.getName() != null) user.setName(receivedUser.getName());
        return user;
    }

    @Override
    public void deleteUser(long id) {
        userMap.remove(id);
    }
}
