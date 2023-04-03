package ru.practicum.shareit.Item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@Sql("/testData.sql")
public class RepositoryTest {

    @Autowired
    private ItemRepository repository;

    @Test
    void getItemById() {
        Item item = repository.getById(100L);

        assertThat(item.getId(), equalTo(100L));
        assertThat(item.getName(), equalTo("test"));
        assertThat(item.getDescription(), equalTo("test description"));
    }

    @Test
    void addItem() {
        Item itemToAdd = new Item();
        itemToAdd.setName("test2");
        itemToAdd.setDescription("test2 description");
        itemToAdd.setOwnerId(100);
        itemToAdd.setAvailable(false);

        Item item = repository.save(itemToAdd);

        assertThat(item.getId(), equalTo(1L));
        assertThat(item.getName(), equalTo("test2"));
        assertThat(item.getDescription(), equalTo("test2 description"));
    }

    @Test
    void changeItem() {
        Item itemToChange = new Item();
        itemToChange.setId(100L);
        itemToChange.setName("test3");
        itemToChange.setDescription("test3 description");
        itemToChange.setOwnerId(100);
        itemToChange.setAvailable(true);

        Item item = repository.save(itemToChange);

        assertThat(item.getId(), equalTo(100L));
        assertThat(item.getName(), equalTo("test3"));
        assertThat(item.getDescription(), equalTo("test3 description"));
        assertThat(item.getAvailable(), equalTo(true));
    }
}
