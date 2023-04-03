package ru.practicum.shareit.request;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.exception.IlligalRequestException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestService {
    private ItemRequestRepository repository;
    private UserService userService;
    private ItemService itemService;
    private final JPAQueryFactory jpaQueryFactory;

    public ItemRequest getItemRequestById(long requestId, long requesterId) {
        userService.getUserById(requesterId);

        return repository.findById(requestId)
                .orElseThrow(() -> new ElementNotFoundException("Request does not exist"));
    }

    public ItemRequest addItemRequest(long requesterId, ItemRequest itemRequest) {
        User requesterFromRepo = userService.getUserById(requesterId);
        if (itemRequest.getDescription() == null ||
                itemRequest.getDescription().isBlank())
            throw new IlligalRequestException("Description of request cannot be empty");

        itemRequest.setRequester(requesterFromRepo);

        return repository.save(itemRequest);
    }

    public List<ItemRequest> getAllItemRequests(long requesterId, @PositiveOrZero int from, @Positive int size) {
        userService.getUserById(requesterId);

        List<ItemRequest> itemRequests = jpaQueryFactory
                .selectFrom(QItemRequest.itemRequest)
                .where(QItemRequest.itemRequest.requester.id.ne(requesterId))
                .orderBy(QItemRequest.itemRequest.created.desc())
                .offset(from)
                .limit(size)
                .fetch();

        return itemRequests.stream()
                .peek(itemRequest -> itemRequest.setItems(itemService.getItemsByRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    public List<ItemRequest> getItemRequestByRequester(long requesterId) {
        userService.getUserById(requesterId);
        List<ItemRequest> itemRequests = IterableUtils.toList(repository.findAll(
                QItemRequest.itemRequest.requester.id.eq(requesterId),
                new QSort(QItemRequest.itemRequest.created.desc())));

        return itemRequests.stream()
                .peek(itemRequest -> itemRequest.setItems(itemService.getItemsByRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }
}
