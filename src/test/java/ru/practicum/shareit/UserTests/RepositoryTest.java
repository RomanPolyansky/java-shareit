package ru.practicum.shareit.UserTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.model.User;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@Sql("/testData.sql")
class RepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void getUser() {
        User userFromRepo = repository.getById(1L);

        assertThat(userFromRepo.getId(), equalTo(1L));
        assertThat(userFromRepo.getName(), equalTo("test"));
        assertThat(userFromRepo.getEmail(), equalTo("test@email.ru"));
    }

    @Test
    void saveUser() {
        User userFromRepo = repository.save(User.builder().id(1).name("test1").email("test_email@a.ru").build());

        assertThat(userFromRepo.getId(), equalTo(1L));
        assertThat(userFromRepo.getName(), equalTo("test1"));
        assertThat(userFromRepo.getEmail(), equalTo("test_email@a.ru"));
    }
}