package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

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
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "booker_id")
    private long bookerId;

    @Column(name = "item_id")
    private long itemId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Booking() {
    }
}
