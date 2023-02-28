package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserStorage userStorage;
    private final ItemStorage storage;

    @Override
    public Item getItemById(long id) {
        Optional<Item> optionalItem = storage.getItemById(id);
        return optionalItem.orElseThrow(
                () -> new NoSuchElementException("Item is not found")
        );
    }

    @Override
    public List<Item> getAll() {
        return storage.getAll();
    }

    @Override
    public Item addItem(Item item) {
        if (!isValidItem(item)) throw new NoSuchElementException("item to add is not valid");
        return storage.addItem(item);
    }

    @Override
    public void deleteItem(long id) {
        if (!exists(id)) throw new NoSuchElementException("item to delete is not found");
        storage.deleteItem(id);
    }

    @Override
    public boolean exists(Item item) {
        return true;
    }

    public boolean exists(long id) {
        return storage.getItemById(id).isPresent();
    }

    public Item changeItem(Item item) {
        checkOwnership(item);
        return storage.changeItem(item);
    }

    @Override
    public boolean isValidItem(Item item) {
        checkOwnership(item);
        return true;
    }

    @Override
    public void checkOwnership(Item item) {
        if (userStorage.getUserById(item.getOwnerId()).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "such user does not exist"
            );
        }
        if (exists(item.getId()) && item.getOwnerId() != getItemById(item.getId()).getOwnerId()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is not owner of this item"
            );
        }
    }

    @Override
    public List<Item> getItemsByOwnerId(long id) {
        return storage.getItemsByOwnerId(id);
    }

    @Override
    public List<Item> searchForItemsByText(String text) {
        return storage.searchForItemsByText(text);
    }
}