package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public User getUserById(long id) {
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.orElseThrow(
                () -> new ElementNotFoundException("user is not found")
        );
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User addUser(User user) {
        return repository.save(user);
    }

    public User changeUser(User user) {
        log.info(user.toString());
        User mergedUser = repository.getById(user.getId()).merge(user);
        return repository.save(mergedUser);
    }

    public void deleteUser(long id) {
        repository.deleteById(id);
    }
}