package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item getItemById(long id);

    Item addItem(Item item);

    Item changeItem(Item item);

    Comment addComment(Comment comment);

    List<Item> getItemsByOwnerId(long ownerId, int from, int size);

    List<Item> searchForItemsByText(String text, int from, int size);

    List<Comment> fetchComments(long itemId);

    List<Item> getItemsByRequestId(long requestId);
}
