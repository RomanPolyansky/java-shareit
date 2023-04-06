package ru.practicum.shareit.Booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.configuration.PagesConfig;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = BookingController.class)
class ControllerTest {

    @MockBean
    private BookingService service;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private final User user = new User(
            1L,
            "userName",
            "user@email.ru"
    );

    private final Item item = Item.builder()
            .id(1L)
            .name("test")
            .description("test")
            .available(true)
            .ownerId(1)
            .build();

    private final Booking booking = new Booking(
            1L,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            item,
            user,
            BookingStatus.WAITING,
            "WAITING"
    );

    private final BookingDtoResponse bookingDtoResponse = BookingMapper.mapBookingToResponse(booking);

    @Test
    @SneakyThrows
    void addBooking() {
        when(service.addBooking(Mockito.any()))
                .thenReturn(booking);

        MvcResult mvcResult = mvc.perform(post("/bookings")
                .header("x-sharer-user-id", user.getId())
                .content(mapper.writeValueAsString(bookingDtoResponse))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(mapper.writeValueAsString(bookingDtoResponse)));
        verify(service).addBooking(Mockito.any());
    }

    @Test
    @SneakyThrows
    void replyBooking() {
        when(service.replyBooking(booking.getId(), "true", user.getId()))
                .thenReturn(booking);

        MvcResult mvcResult = mvc.perform(patch("/bookings/{bookingId}", booking.getId())
                .header("x-sharer-user-id", user.getId())
                .param("approved", "true")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(mapper.writeValueAsString(bookingDtoResponse)));
        verify(service).replyBooking(booking.getId(), "true", user.getId());
    }

    @Test
    @SneakyThrows
    void getBooking() {
        when(service.getBookingById(booking.getId(), user.getId()))
                .thenReturn(booking);

        MvcResult mvcResult = mvc.perform(get("/bookings/{bookingId}", booking.getId())
                .header("x-sharer-user-id", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(mapper.writeValueAsString(bookingDtoResponse)));
        verify(service).getBookingById(booking.getId(), user.getId());
    }

    @Test
    @SneakyThrows
    void getAllBookingsOfUserItems() {
        when(service.getAllBookingsOfOwnerItems(user.getId(), BookingState.ALL.name(),
                                0, Integer.parseInt(PagesConfig.DEFAULT_SIZE_AS_STRING)))
                .thenReturn(List.of(booking));

        MvcResult mvcResult = mvc.perform(get("/bookings/owner")
                .header("x-sharer-user-id", user.getId())
                .param("state", "ALL")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(),
                equalTo(mapper.writeValueAsString(List.of(bookingDtoResponse))));
        verify(service).getAllBookingsOfOwnerItems(user.getId(), BookingState.ALL.name(),
                                0, Integer.parseInt(PagesConfig.DEFAULT_SIZE_AS_STRING));
    }

    @Test
    @SneakyThrows
    void getAllBookingsOfUser() {

        when(service.getAllBookingsOfUser(user.getId(), BookingState.ALL.name(),
                                0, Integer.parseInt(PagesConfig.DEFAULT_SIZE_AS_STRING)))
                .thenReturn(List.of(booking));

        MvcResult mvcResult = mvc.perform(get("/bookings")
                .header("x-sharer-user-id", user.getId())
                .param("state", "ALL")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(),
                equalTo(mapper.writeValueAsString(List.of(bookingDtoResponse))));
        verify(service).getAllBookingsOfUser(user.getId(), BookingState.ALL.name(),
                                 0, Integer.parseInt(PagesConfig.DEFAULT_SIZE_AS_STRING));

    }
}