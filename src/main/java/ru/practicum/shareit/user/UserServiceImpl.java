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

    private final UserRepository repository;

    public boolean isValidUser(User receivedUser) {
        return repository.findByEmail(receivedUser.getEmail()).isEmpty();
    }

    public User getUserById(long id) {
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.orElseThrow(
                () -> new NoSuchElementException("user is not found")
        );
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User addUser(User user) {
        if (!isValidUser(user)) throw new NoSuchElementException("user to add is not valid");
        return repository.save(user);
    }

    public User changeUser(User user) {
        if (!exists(user)) throw new NoSuchElementException("user to change is not found");
        if (!isValidUser(user)) throw new NoSuchElementException("user to change is not valid");
        User mergedUser = repository.getById(user.getId()).merge(user);
        return repository.save(mergedUser);
    }

    public void deleteUser(long id) {
        if (!exists(id)) throw new NoSuchElementException("user to delete is not found");
        repository.deleteById(id);
    }

    public boolean exists(long id) {
        return repository.findById(id).isPresent();
    }

    public boolean exists(User user) {
        return repository.findById(user.getId()).isPresent();
    }
}