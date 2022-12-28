package ru.practicum.shareit.item;

import ru.practicum.shareit.abstracts.GeneralService;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemService extends GeneralService<Item> {

    List<Item> getItemsByOwnerId(long id);

    List<Item> searchForItemsByText(String text);
}
