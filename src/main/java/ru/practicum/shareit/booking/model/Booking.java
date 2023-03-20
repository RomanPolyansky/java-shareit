package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    @Transient
    private Status status;

    @Column(name = "status")
    private String statusStr;

    public Booking() {
        super();
        status = Status.WAITING;
        statusStr = Status.WAITING.toString();
    }

    public Booking merge(Booking other) {
        if (other.startDate != null) startDate = other.startDate;
        if (other.endDate != null) endDate = other.endDate;
        if (other.status != null) status = other.status;
        return this;
    }

    @PreUpdate
    @PostLoad
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
