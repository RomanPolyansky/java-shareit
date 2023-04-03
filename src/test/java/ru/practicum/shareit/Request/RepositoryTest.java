package ru.practicum.shareit.Request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@Sql("/testData.sql")
public class RepositoryTest {

    @Autowired
    private ItemRequestRepository repository;

    @Test
    void getItemRequestById() {
        ItemRequest itemRequest = repository.getById(100L);

        assertThat(itemRequest.getId(), equalTo(100L));
        assertThat(itemRequest.getDescription(), equalTo("test"));
    }

    @Test
    void addItemRequest() {
        ItemRequest itemRequestToAdd = new ItemRequest();

        itemRequestToAdd.setDescription("test itemRequest description");
        itemRequestToAdd.setRequester(User.builder().id(100L).build());
        itemRequestToAdd.setCreated(LocalDateTime.now());

        ItemRequest itemRequest = repository.save(itemRequestToAdd);

        assertThat(itemRequest.getId(), equalTo(1L));
        assertThat(itemRequest.getDescription(), equalTo("test itemRequest description"));
    }
}
