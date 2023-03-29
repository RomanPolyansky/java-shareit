package ru.practicum.shareit.ItemTests;

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
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ControllerTest {

    @MockBean
    private ItemService itemService;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    private final User user = new User(
            1L,
            "userName",
            "user@email.ru"
    );

    private final Item item = new Item(
            1L,
            "test",
            "test",
            true,
            1L
    );

    @Test
    @SneakyThrows
    void getItemById() {
        Long itemId = 1L;
        when(itemService.getItemById(Mockito.anyLong()))
                .thenReturn(item);

        MvcResult mvcResult = mvc.perform(get("/items/{id}", itemId)
                .header("x-sharer-user-id", user.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(mapper.writeValueAsString(ItemMapper.mapItemToResponse(item))));
        verify(itemService).getItemById(itemId);
    }

    @Test
    @SneakyThrows
    void getItems() {
        List<ItemDto> items = List.of(ItemMapper.mapItemToResponse(item));
        when(itemService.getItemsByOwnerId(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(item));

        MvcResult mvcResult = mvc.perform(get("/items")
                .header("x-sharer-user-id", user.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(mapper.writeValueAsString(items)));
    }

    @Test
    @SneakyThrows
    void getItemsByText() {
        List<ItemDto> items = List.of(ItemMapper.mapItemToResponse(item));
        when(itemService.searchForItemsByText(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(item));

        MvcResult mvcResult = mvc.perform(get("/items/search")
                        .header("x-sharer-user-id", user.getId())
                        .param("text", "es")).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(mapper.writeValueAsString(items)));
        verify(itemService).searchForItemsByText(Mockito.anyString(), Mockito.any(),Mockito.any());
    }

    @Test
    @SneakyThrows
    void create() {
        when(itemService.addItem(Mockito.any()))
                .thenReturn(item);

        mvc.perform(post("/items")
                        .header("x-sharer-user-id", user.getId())
                        .content(mapper.writeValueAsString(ItemMapper.mapItemToResponse(item)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));

        verify(itemService).addItem(Mockito.any());
    }


    @Test
    @SneakyThrows
    void addComment() {
        Long itemId = item.getId();
        Comment comment = new Comment(1L, "test", LocalDateTime.now(), item, user);
        when(itemService.addComment(Mockito.any()))
                .thenReturn(comment);

        mvc.perform(post("/items/{id}/comment", itemId)
                        .header("x-sharer-user-id", user.getId())
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText())));

        verify(itemService).addComment(Mockito.any());
    }

    @Test
    @SneakyThrows
    void update() {
        when(itemService.changeItem(Mockito.any()))
                .thenReturn(item);

        mvc.perform(patch("/items/{id}", item.getId())
                        .header("x-sharer-user-id", user.getId())
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));
        verify(itemService).changeItem(Mockito.any());
    }
}