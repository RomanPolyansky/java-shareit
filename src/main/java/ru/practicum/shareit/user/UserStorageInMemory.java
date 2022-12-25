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
    public Optional<User> getEntityById(long id) {
        return Optional.of(userMap.get(id));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User addEntity(User entity) {
        entity.setId(currentId++);
        userMap.put(entity.getId(), entity);
        return userMap.get(entity.getId());
    }

    @Override
    public User changeEntity(User entity) {
        User user = userMap.get(entity.getId());
        if (entity.getEmail() != null) user.setEmail(entity.getEmail());
        if (entity.getName() != null) user.setName(entity.getName());
        return user;
    }

    @Override
    public void deleteEntity(long id) {
        userMap.remove(id);
    }
}
