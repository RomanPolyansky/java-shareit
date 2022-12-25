package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.abstracts.ServiceAbstract;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ItemServiceImpl extends ServiceAbstract<Item, ItemStorage> implements ItemService {

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage) {
        this.storage = itemStorage;
    }

    @Override
    public Item changeEntity(Item entity) {
        checkOwnership(entity);
        storage.changeEntity(entity);
        return super.changeEntity(entity);
    }

    private void checkOwnership(Item entity) {
        if (entity.getOwnerId() != getEntityById(entity.getId()).getOwnerId()){
            throw new AccessDeniedException("User is not owner of this item");
        }
    }

    public List<Item> getItemsByOwnerId(long id) {
        return storage.getItemsByOwnerId(id);
    }

    @Override
    public boolean exists(Item entity) {
        return storage.getEntityById(entity.getId()) != null;
    }
}