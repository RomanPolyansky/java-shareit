package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepositoryCustom;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    List<Item> getItemsByOwnerId(long id);

//    Optional<Item> getItemById(long id);
//    List<Item> getAll();
//    Item addItem(Item item);
//    Item changeItem(Item item);
//    void deleteItem(long id);

}