package ru.practicum.shareit.item;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final ItemRepository itemRepository;

    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> searchForItemsByText(String text) {
        List<Item> items = itemRepository.findAll();
        List<Item> matchingItems = items.stream()
                .filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                              i.getName().toLowerCase().contains(text.toLowerCase()))
                .filter(i -> i.getAvailable())
                .collect(Collectors.toList());
        return matchingItems;
    }
}