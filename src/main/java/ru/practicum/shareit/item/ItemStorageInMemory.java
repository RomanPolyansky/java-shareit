package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemStorageInMemory implements ItemStorage {

    private final Map<Long, Item> itemMap = new HashMap<>();
    private long currentId = 1;

    public Optional<Item> getItemById(long id) {
        return Optional.ofNullable(itemMap.get(id));
    }

    public List<Item> getAll() {
        return new ArrayList<>(itemMap.values());
    }

    public Item addItem(Item item) {
        item.setId(currentId++);
        itemMap.put(item.getId(), item);
        return item;
    }

    public Item changeItem(Item receivedItem) {
        Item item = itemMap.get(receivedItem.getId());
        if (receivedItem.getDescription() != null) item.setDescription(receivedItem.getDescription());
        if (receivedItem.getName() != null) item.setName(receivedItem.getName());
        if (receivedItem.getAvailable() != null) item.setAvailable(receivedItem.getAvailable());
        return item;
    }

    public void deleteItem(long id) {
        itemMap.remove(id);
    }

    public List<Item> getItemsByOwnerId(long id) {
        List<Item> ownerItems = new ArrayList<>();

        itemMap.forEach((k, v) -> {
            if (v.getOwnerId() == id) {
                ownerItems.add(v);
            }
        });
        return ownerItems;
    }

    public List<Item> searchForItemsByText(String text) {
        List<Item> resultList = new ArrayList<>();
        if (text.length() == 0) return Collections.emptyList();
        for (Item item : itemMap.values()) {
            if (!item.getAvailable()) continue;
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase()) ) {
                resultList.add(item);
            }
        }
        return resultList;
    }
}
