package ru.practicum.shareit.Booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@Sql("/testData.sql")
public class RepositoryTest {

    @Autowired
    private BookingRepository repository;

    @Test
    void getBooking() {
        Booking booking = repository.getById(100L);

        assertThat(booking.getId(), equalTo(100L));
    }

    @Test
    void addBooking() {
        Booking bookingToAdd = new Booking();
        bookingToAdd.setStartDate(LocalDateTime.now());
        bookingToAdd.setId(1L);
        bookingToAdd.setEndDate(LocalDateTime.now().plusDays(1));
        bookingToAdd.setBooker(User.builder().id(100L).build());
        bookingToAdd.setItem(Item.builder().id(100L).build());

        Booking booking = repository.save(bookingToAdd);

        assertThat(booking.getId(), equalTo(1L));
        assertThat(booking.getBooker().getId(), equalTo(100L));
        assertThat(booking.getItem().getId(), equalTo(100L));
    }
}
