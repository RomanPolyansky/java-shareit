package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    List<Item> getItemsByOwnerId(long id );

    List<Item> searchForItemsByText(String text);

    Optional<Item> getItemById(long id);
    List<Item> getAll();
    Item addItem(Item item);
    Item changeItem(Item item);
    void deleteItem(long id);
}
