package ru.practicum.shareit.item;

import ru.practicum.shareit.abstracts.GeneralStorage;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage extends GeneralStorage<Item> {
    List<Item> getItemsByOwnerId(long id );

    List<Item> searchForItemsByText(String text);
}
