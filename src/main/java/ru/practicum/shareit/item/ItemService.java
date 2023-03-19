package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> getItemsByOwnerId(long id);
    List<Item> searchForItemsByText(String text);
    Item getItemById(long id);
    List<Item> getAll();
    Item addItem(Item item);
    Item changeItem(Item item);
    void deleteItem(long id);
    void checkOwnership(Item item);
}
