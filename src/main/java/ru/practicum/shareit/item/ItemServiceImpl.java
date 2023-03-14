package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository repository;

    @Override
    public Item getItemById(long id) {
        Optional<Item> optionalItem = repository.findById(id);
        return optionalItem.orElseThrow(
                () -> new NoSuchElementException("Item is not found")
        );
    }

    @Override
    public List<Item> getAll() {
        return repository.findAll();
    }

    @Override
    public Item addItem(Item item) {
        if (!isValidItem(item)) throw new NoSuchElementException("item to add is not valid");
        return repository.save(item);
    }

    @Override
    public void deleteItem(long id) {
        if (!exists(id)) throw new NoSuchElementException("item to delete is not found");
        repository.deleteById(id);
    }

    @Override
    public Item changeItem(Item item) {
        checkOwnership(item);
        Item mergedItem = repository.getById(item.getId()).merge(item);
        return repository.save(mergedItem);
    }

    @Override
    public boolean isValidItem(Item item) {
        checkOwnership(item);
        return true;
    }

    @Override
    public boolean exists(long id) {
        return repository.findById(id).isPresent();
    }

    @Override
    public void checkOwnership(Item item) {
        if (userRepository.findById(item.getOwnerId()).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "such user does not exist"
            );
        }
        if (exists(item.getId()) && item.getOwnerId() != getItemById(item.getId()).getOwnerId()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is not owner of this item"
            );
        }
    }

    @Override
    public List<Item> getItemsByOwnerId(long id) {
        return repository.getItemsByOwnerId(id);
    }

    @Override
    public List<Item> searchForItemsByText(String text) {
        return repository.searchForItemsByText(text);
    }
}