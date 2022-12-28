package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.abstracts.ServiceAbstract;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

@Service
public class ItemServiceImpl extends ServiceAbstract<Item, ItemStorage> implements ItemService {

    final UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.storage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Item changeEntity(Item entity) {
        checkOwnership(entity);
        return storage.changeEntity(entity);
    }

    @Override
    public boolean isValidEntity(Item entity) {
        checkOwnership(entity);
        return true;
    }

    private void checkOwnership(Item entity) {
        if (userStorage.getEntityById(entity.getOwnerId()).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "such user does not exist"
            );
        }
        if (exists(entity.getId()) && entity.getOwnerId() != getEntityById(entity.getId()).getOwnerId()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is not owner of this item"
            );
        }
    }

    public List<Item> getItemsByOwnerId(long id) {
        return storage.getItemsByOwnerId(id);
    }

    @Override
    public List<Item> searchForItemsByText(String text) {
        return storage.searchForItemsByText(text);
    }
}