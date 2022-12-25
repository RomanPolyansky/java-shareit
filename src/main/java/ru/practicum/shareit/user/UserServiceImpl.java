package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.abstracts.ServiceAbstract;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceAbstract<User, UserStorage> implements UserService {

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.storage = userStorage;
    }

    @Override
    public boolean isValidEntity(User entity) {
        for (User user : storage.getAll()) {
            if (user.getEmail().equals(entity.getEmail())) return false;
        }
        return true;
    }

    @Override
    public boolean exists(User entity) {
        return storage.getEntityById(entity.getId()) != null;
    }
}