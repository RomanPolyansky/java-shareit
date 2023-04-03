package ru.practicum.shareit.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.model.UserResponse;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class ControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingServiceImpl bookingService;

    private final Long userId = 1L;

    private final User user = new User(
            userId,
            "userName",
            "user@email.ru"
    );

    private final UserResponse userDto = UserMapper.mapUserToResponse(user);

    @Test
    @SneakyThrows
    void getUserById() {
        when(userService.getUserById(Mockito.anyLong()))
                .thenReturn(user);

        mvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDto)));

        verify(userService).getUserById(userId);
    }

    @Test
    @SneakyThrows
    void getAllUsers() {
        when(userService.getAll())
                .thenReturn(List.of(user));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json((mapper.writeValueAsString(List.of(userDto)))));

        verify(userService).getAll();
    }

    @Test
    @SneakyThrows
    void addUser() {
        when(userService.addUser(Mockito.any()))
                .thenReturn(user);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class));
    }

    @Test
    @SneakyThrows
    void updateUser() {
        when(userService.changeUser(Mockito.any())).thenReturn(user);
        mvc.perform(patch("/users/{id}", userId)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class));
    }

    @Test
    @SneakyThrows
    void deleteUser() {
        mvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(status().isOk());
    }
}