package ru.practicum.shareit.controllerEndpointTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.format.Formatter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();
    @InjectMocks
    private BookingController controller;
    @Autowired
    private MockMvc mvc;
    private BookingDto bookingDto;
    private BookingDtoResponse bookingDtoResponse;
    private User user;
    @InjectMocks
    private final BookingService bookingService;

    @Autowired
    public BookingControllerTest(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @BeforeEach
    void initElements() {

        user = new User();
        user.setId(1L);
        user.setEmail("test-email@email.com");
        user.setName("TestName");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("book");
        itemDto.setDescription("java for dummies");
        itemDto.setAvailable(true);

        bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.parse("2030-03-26T19:00:00.000000"));
        bookingDto.setEnd(LocalDateTime.parse("2030-03-29T19:00:00.000000"));

        /*
        bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(1);
        bookingDtoResponse.setBooker(user);
        bookingDtoResponse.setItem(itemDto);
        bookingDtoResponse.setStatus(Status.WAITING);
        bookingDtoResponse.setStart(LocalDateTime.parse("2030-03-26T19:00:00.000000"));
        bookingDtoResponse.setEnd(LocalDateTime.parse("2030-03-29T19:00:00.000000"));
        */
    }

    @BeforeEach
    public void setUpConfForDataTimeFormat() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(UnsupportedStatusException.class)
                .build();
    }

    @Test
    void createBooking_whenValidRequest_thenOkAndReturnTheBooking() throws Exception {
        Mockito.when(controller.addBooking(Mockito.any(BookingDto.class), anyLong()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is(Status.WAITING.toString())))
                .andExpect(jsonPath("$.item.name", is("book")))
                .andExpect(jsonPath("$.item.id", is(2)))
                .andExpect(jsonPath("$.start", is("2030-01-31T19:53:19.363093")))
                .andExpect(jsonPath("$.start", is("2030-01-31T19:53:19.363093")))
                .andExpect(jsonPath("$.booker.id", is(3)));
    }

    /*

    @Test
    void createBookingOrder_whenEmptyBody_thenBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .content(mapper.writeValueAsString(new BookingDtoResponse()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBookingOrder_whenEmptyUserHeader_thenBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBookingOrder_whenItemIsNotAvailable_thenBadRequest() throws Exception {
        Mockito.when(bookingService.createBookingOrder(Mockito.any(), anyLong()))
                .thenThrow(ItemNotAvailableForBookingException.class);

        mvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(nullValue()))
                .andExpect(jsonPath("$.errorInfo.type").value("logic"))
                .andExpect(jsonPath("$.errorInfo.description")
                        .value("object does not available for booking"));
    }

    @Test
    void createBookingOrder_whenOwnerBookOwnItem_thenNotFound() throws Exception {
        Mockito.when(bookingService.createBookingOrder(Mockito.any(), anyLong()))
                .thenThrow(ItemCanNotBeBookedByOwnerException.class);

        mvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(nullValue()))
                .andExpect(jsonPath("$.errorInfo.type").value("logic"))
                .andExpect(jsonPath("$.errorInfo.description")
                        .value("item can not be booked by owner"));
    }

    @Test
    void reactBookingOrder_whenValidRequest_thenOKt() throws Exception {
        Mockito.when(bookingService.reactBookingOrder(anyLong(), anyLong(), eq(true)))
                .thenReturn(bookingDto);

        bookingDto.setStatus(Status.APPROVED);

        mvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingIs}", 1)
                        .header("X-Sharer-User-Id", 3)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.booker").isNotEmpty());
    }

    @Test
    void reactBookingOrder_whenItemHasNotStatusWaiting_thenBadRequest() throws Exception {
        Mockito.when(bookingService.reactBookingOrder(anyLong(), anyLong(), eq(true)))
                .thenThrow(ItemNotAvailableForBookingException.class);

        mvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingIs}", 1)
                        .header("X-Sharer-User-Id", 3)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(nullValue()))
                .andExpect(jsonPath("$.errorInfo.type").value("logic"))
                .andExpect(jsonPath("$.errorInfo.description")
                        .value("object does not available for booking"));
    }

    @Test
    void reactBookingOrder_whenReactNotOwner_thenNotFound() throws Exception {
        Mockito.when(bookingService.reactBookingOrder(anyLong(), anyLong(), eq(true)))
                .thenAnswer(invocation -> {
                    throw new AccessDeniedException("");
                });

        mvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingIs}", 1)
                        .header("X-Sharer-User-Id", 3)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(""))
                .andExpect(jsonPath("$.errorInfo.type").value("common"))
                .andExpect(jsonPath("$.errorInfo.description").value("no detailed exception"));
    }

    @Test
    void reactBookingOrder_whenRequestWithoutParams_thenBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .patch("/bookings/{bookingIs}", 1)
                        .header("X-Sharer-User-Id", 3)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Required request parameter 'approved' for method parameter type Boolean is not present"))
                .andExpect(jsonPath("$.errorInfo.type").value("validation"))
                .andExpect(jsonPath("$.errorInfo.description").value("request parameter is missing"));
    }

    @Test
    void getAllAuthorBookingOrders_whenRequestWithoutState_thenOkAndUseAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/bookings")
                        .header("X-Sharer-User-Id", 3)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1))
                .getAllAuthorBookingOrder(3L, "ALL", Optional.empty(), Optional.empty());
    }

    @Test
    void getAllOwnerBookingOrders_whenRequestWithoutState_thenOkAndUseAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/bookings/owner")
                        .header("X-Sharer-User-Id", 3)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1))
                .getAllOwnerBookingOrder(3L, "ALL", Optional.empty(), Optional.empty());
    }

     */
}
