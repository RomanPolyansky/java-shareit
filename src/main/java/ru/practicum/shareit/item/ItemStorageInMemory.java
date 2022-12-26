package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemStorageInMemory implements ItemStorage {

    private final Map<Long, Item> itemMap = new HashMap<>();
    private long currentId = 1;

    @Override
    public Optional<Item> getEntityById(long id) {
        return Optional.ofNullable(itemMap.get(id));
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(itemMap.values());
    }

    @Override
    public Item addEntity(Item entity) {
        entity.setId(currentId++);
        itemMap.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Item changeEntity(Item entity) {
        Item item = itemMap.get(entity.getId());
        if (entity.getDescription() != null) item.setDescription(entity.getDescription());
        if (entity.getName() != null) item.setName(entity.getName());
        if (entity.getIsAvailable() != null) item.setIsAvailable(entity.getIsAvailable());
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

    @Override
    public List<Item> searchForItemsByText(String text) {
        List<Item> resultList = new ArrayList<>();
        if (text.length() == 0) return Collections.emptyList();
        for (Item item : itemMap.values()) {
            if (!item.getIsAvailable()) continue;
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase()) ) {
                resultList.add(item);
            }
        }
        return resultList;
    }
}
