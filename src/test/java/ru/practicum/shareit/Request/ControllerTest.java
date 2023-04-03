package ru.practicum.shareit.Request;

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
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ItemRequestController.class)
class ControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookingServiceImpl bookingService;
    @MockBean
    private ItemServiceImpl itemService;
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

    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .description("test")
            .requester(user)
            .created(LocalDateTime.now())
            .items(List.of(item))
            .build();

    private final ItemRequestDto itemRequestDto = ItemRequestMapper.mapItemRequestToResponse(itemRequest);

    @Test
    @SneakyThrows
    void createItemRequest() {

        when(itemRequestService.addItemRequest(Mockito.anyLong(), Mockito.any()))
                .thenReturn(itemRequest);

        MvcResult mvcResult = mvc.perform(post("/requests")
                .header("x-sharer-user-id", user.getId())
                .content(mapper.writeValueAsString(itemRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(mapper.writeValueAsString(itemRequestDto)));
        verify(itemRequestService).addItemRequest(Mockito.anyLong(), Mockito.any());
    }

    @Test
    @SneakyThrows
    void getAllItemRequests() {
        when(itemRequestService.getAllItemRequests(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(itemRequest));

        MvcResult mvcResult = mvc.perform(get("/requests/all")
                .header("x-sharer-user-id", user.getId())
                .param("from", "0")
                .param("size", "10")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(mapper.writeValueAsString(List.of(itemRequestDto))));
        verify(itemRequestService).getAllItemRequests(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    @SneakyThrows
    void getItemRequestById() {
        when(itemRequestService.getItemRequestById(itemRequest.getId(), user.getId()))
                .thenReturn(itemRequest);
        when(itemService.getItemsByRequestId(itemRequest.getId()))
                .thenReturn(List.of(item));

        MvcResult mvcResult = mvc.perform(get("/requests/{requestId}", itemRequest.getId())
                .header("x-sharer-user-id", user.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(mapper.writeValueAsString(itemRequestDto)));
        verify(itemRequestService).getItemRequestById(itemRequest.getId(), user.getId());
    }

    @Test
    @SneakyThrows
    void getItemRequestByRequester() {
        when(itemRequestService.getItemRequestByRequester(user.getId()))
                .thenReturn(List.of(itemRequest));
        when(itemService.getItemsByRequestId(itemRequest.getId()))
                .thenReturn(List.of(item));

        MvcResult mvcResult = mvc.perform(get("/requests")
                .header("x-sharer-user-id", user.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString(), equalTo(mapper.writeValueAsString(List.of(itemRequestDto))));
        verify(itemRequestService).getItemRequestByRequester(user.getId());
    }
}