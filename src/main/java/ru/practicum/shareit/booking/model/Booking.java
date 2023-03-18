package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * TODO Sprint add-bookings.
 */

@Entity
@Table(name = "bookings")
@Data
@Builder
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime  endDate;

    @Column(name = "booker_id")
    private long bookerId;

    @Column(name = "item_id")
    private long itemId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Booking() {
    }

    public Booking merge(Booking other) {
        if (other.startDate != null) startDate = other.startDate;
        if (other.endDate != null) endDate = other.endDate;
        if (other.status != null) status = other.status;
        return this;
    }

    public void recalculateStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDate)) {
            status = Status.FUTURE;
        } else if (now.isAfter(endDate)) {
            status = Status.PAST;
        } else {
            status = Status.CURRENT;
        }
    }
}
