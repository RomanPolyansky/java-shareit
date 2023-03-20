package ru.practicum.shareit.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QComment;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Lazy private final BookingService bookingService;

    @Override
    public Item getItemById(long id, long requesterId) {
        Optional<Item> optionalItem = repository.findById(id);
        if (optionalItem.isEmpty()) throw new NoSuchElementException("Item does not exist");
        Item item = optionalItem.get();
        item.setComments(fetchComments(item.getId()));
        fetchBookings(item, requesterId);
        return item;
    }

    @Override
    public Item getItemByIdShort(long id) {
        Optional<Item> optionalItem = repository.findById(id);
        if (optionalItem.isEmpty()) throw new NoSuchElementException("Item does not exist");
        Item item = optionalItem.get();
        return item;
    }

    private void fetchBookings(Item item, long requesterId) {
        if (requesterId == item.getOwnerId()) {
            item.setNextBooking(bookingService.getNextBooking(item.getId()));
            item.setLastBooking(bookingService.getLastBooking(item.getId()));
        }
    }

    @Override
    public List<Item> getAll() {
        return repository.findAll();
    }

    @Override
    public Item addItem(Item item) {
        userService.getUserById(item.getOwnerId());
        return repository.save(item);
    }


    @Override
    public void deleteItem(long id) {
        repository.deleteById(id);
    }

    @Override
    public Item changeItem(Item item) {
        checkOwnership(item);
        Item mergedItem = repository.getById(item.getId()).merge(item);
        return repository.save(mergedItem);
    }

    @Override
    public void checkOwnership(Item item) {
        userService.getUserById(item.getOwnerId());
        if (repository.findById(item.getId()).isPresent() && item.getOwnerId() != getItemByIdShort(item.getId()).getOwnerId()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User is not owner of this item"
            );
        }
    }

    @Override
    public Comment addCommment(Comment comment) {
        Item item = repository.getById(comment.getItem().getId());
        User booker = getBookerOfItem(item, comment.getAuthor().getId());

        comment.setAuthor(booker);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> fetchComments(long itemId) {
        List<Comment> comments = jpaQueryFactory.selectFrom(QComment.comment)
                .where(QComment.comment.item.id.eq(itemId))
                .orderBy(QComment.comment.created.asc())
                .fetch();
        return comments;
    }

    @Override
    public List<Item> getItemsByOwnerId(long id) {
        return repository.getItemsByOwnerId(id);
    }

    @Override
    public List<Item> searchForItemsByText(String text) {
        if (text.isBlank()) throw new NoSuchElementException("text cannot be empty");
        return repository.searchForItemsByText(text);
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
            throw new NoSuchElementException("User did not book this item");
        }
        return matchingBooking.getBooker();
    }
}