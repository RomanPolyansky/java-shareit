package ru.practicum.shareit.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.configuration.PagesConfig;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.exception.IlligalRequestException;
import ru.practicum.shareit.exception.NoAccessException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QComment;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository repository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public Item getItemById(long id) {
        return repository.findById(id).orElseThrow(() -> new ElementNotFoundException("Item does not exist"));
    }

    public Item addItem(Item item) {
        userService.getUserById(item.getOwnerId());
        return repository.save(item);
    }

    public Item changeItem(Item item) {
        checkOwnership(item);
        Item mergedItem = repository.getById(item.getId()).merge(item);
        return repository.save(mergedItem);
    }

    public void checkOwnership(Item item) {
        userService.getUserById(item.getOwnerId());
        if (repository.findById(item.getId()).isPresent()
                && item.getOwnerId() != repository.getById(item.getId()).getOwnerId()) {
            throw new NoAccessException("Not an owner of an item");
        }
    }

    public Comment addComment(Comment comment) {
        Item item = repository.getById(comment.getItem().getId());
        User booker = getBookerOfItem(item, comment.getAuthor().getId());

        comment.setAuthor(booker);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public List<Comment> fetchComments(long itemId) {
        return jpaQueryFactory.selectFrom(QComment.comment)
                .where(QComment.comment.item.id.eq(itemId))
                .orderBy(QComment.comment.created.asc())
                .fetch();
    }

    public List<Item> getItemsByOwnerId(long ownerId, Optional<Integer> from, Optional<Integer> size) {
        return IterableUtils.toList(jpaQueryFactory.selectFrom(QItem.item)
                .where(QItem.item.ownerId.eq(ownerId))
                .orderBy(QItem.item.id.asc())
                .offset(from.orElse(0))
                .limit(size.orElse(PagesConfig.DEFAULT_SIZE))
                .fetch());
    }

    public List<Item> searchForItemsByText(String text, @PositiveOrZero Optional<Integer> from, @Positive Optional<Integer> size) {
        if (text.isBlank()) return Collections.emptyList();
        return IterableUtils.toList(jpaQueryFactory.selectFrom(QItem.item)
                .where(QItem.item.description.containsIgnoreCase(text)
                        .or(QItem.item.name.containsIgnoreCase(text)))
                .where(QItem.item.available.eq(true))
                .orderBy(QItem.item.id.asc())
                .offset(from.orElse(0))
                .limit(size.orElse(PagesConfig.DEFAULT_SIZE))
                .fetch());
    }

    private User getBookerOfItem(Item item, long bookerId) {
        Booking matchingBooking = jpaQueryFactory
                .selectFrom(QBooking.booking)
                .where(QBooking.booking.item.id.eq(item.getId()))
                .where(QBooking.booking.booker.id.eq(bookerId))
                .where(QBooking.booking.startDate.before(LocalDateTime.now()))
                .where(QBooking.booking.statusStr.ne(Status.REJECTED.toString()))
                .fetchFirst();
        if (matchingBooking == null) {
            throw new IlligalRequestException("User did not book this item");
        }
        return matchingBooking.getBooker();
    }

    public List<Item> getItemsByRequestId(long requestId) {
        return IterableUtils.toList(
                repository.findAll(
                    QItem.item.itemRequest.id.eq(requestId),
                    new QSort(QItem.item.itemRequest.created.desc())));
    }
}