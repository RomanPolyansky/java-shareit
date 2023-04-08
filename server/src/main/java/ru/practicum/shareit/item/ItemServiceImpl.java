package ru.practicum.shareit.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.exception.IlligalRequestException;
import ru.practicum.shareit.exception.NoAccessException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QComment;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Item getItemById(long id) {
        return itemRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Item does not exist"));
    }

    @Override
    public Item addItem(Item item) {
        userService.getUserById(item.getOwnerId());
        return itemRepository.save(item);
    }

    @Override
    public Item changeItem(Item item) {
        checkOwnership(item);
        Item mergedItem = itemRepository.getById(item.getId()).merge(item);
        return itemRepository.save(mergedItem);
    }

    @Override
    public Comment addComment(Comment comment) {
        Item item = itemRepository.getById(comment.getItem().getId());
        User booker = getBookerOfItem(item, comment.getAuthor().getId());

        comment.setAuthor(booker);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Override
    public List<Item> getItemsByOwnerId(long ownerId, int from, int size) {
        return IterableUtils.toList(jpaQueryFactory.selectFrom(QItem.item)
                .where(QItem.item.ownerId.eq(ownerId))
                .orderBy(QItem.item.id.asc())
                .offset(from)
                .limit(size)
                .fetch());
    }

    @Override
    public List<Item> searchForItemsByText(String text, int from, int size) {
        if (text.isBlank()) return Collections.emptyList();
        return IterableUtils.toList(jpaQueryFactory.selectFrom(QItem.item)
                .where(QItem.item.description.containsIgnoreCase(text)
                        .or(QItem.item.name.containsIgnoreCase(text)))
                .where(QItem.item.available.eq(true))
                .orderBy(QItem.item.id.asc())
                .offset(from)
                .limit(size)
                .fetch());
    }

    public void checkOwnership(Item item) {
        userService.getUserById(item.getOwnerId());
        if (itemRepository.findById(item.getId()).isPresent()
                && item.getOwnerId() != itemRepository.getById(item.getId()).getOwnerId()) {
            throw new NoAccessException("Not an owner of an item");
        }
    }

    private User getBookerOfItem(Item item, long bookerId) {
        Booking matchingBooking = jpaQueryFactory
                .selectFrom(QBooking.booking)
                .where(QBooking.booking.item.id.eq(item.getId()))
                .where(QBooking.booking.booker.id.eq(bookerId))
                .where(QBooking.booking.startDate.before(LocalDateTime.now()))
                .where(QBooking.booking.statusStr.ne(BookingStatus.REJECTED.toString()))
                .fetchFirst();
        if (matchingBooking == null) {
            throw new IlligalRequestException("User did not book this item");
        }
        return matchingBooking.getBooker();
    }

    @Override
    public List<Comment> fetchComments(long itemId) {
        return jpaQueryFactory.selectFrom(QComment.comment)
                .where(QComment.comment.item.id.eq(itemId))
                .orderBy(QComment.comment.created.asc())
                .fetch();
    }

    public List<Item> getItemsByRequestId(long requestId) {
        return IterableUtils.toList(
                itemRepository.findAll(
                        QItem.item.itemRequest.id.eq(requestId),
                        new QSort(QItem.item.itemRequest.created.desc())));
    }
}