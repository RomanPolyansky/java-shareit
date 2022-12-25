package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class ItemStorageInMemory implements ItemStorage {

    private final Map<Long, Item> itemMap = new HashMap<>();
    private long currentId = 1;

    @Override
    public Optional<Item> getEntityById(long id) {
        return Optional.of(itemMap.get(id));
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(itemMap.values());
    }

    @Override
    public Item addEntity(Item entity) {
        itemMap.put(currentId++, entity);
        return entity;
    }

    @Override
    public Item changeEntity(Item entity) {
        Item item = itemMap.get(entity.getId());
        if (entity.getDescription() != null) item.setDescription(entity.getDescription());
        if (entity.getName() != null) item.setName(entity.getName());
        return item;
    }

    @Override
    public void deleteEntity(long id) {
        itemMap.remove(id);
    }

    @Override
    public List<Item> getItemsByOwnerId(long id) {
        List<Item> ownerItems = new ArrayList<>();
        for (Item item : itemMap.values()) {
            if (item.getOwnerId() == id) {
                ownerItems.add(item);
            }
        }
        return ownerItems;
    }
}
