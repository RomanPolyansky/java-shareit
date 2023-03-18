package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;

import javax.persistence.*;

@Entity
@Table(name = "items")
@Data
@Builder
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "owner_id")
    long ownerId;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "is_available")
    Boolean available;

    public Item() {
    }

    public Item merge(Item other) {
        if (other.name != null) name = other.name;
        if (other.description != null) description = other.description;
        if (other.available != null) available = other.available;
        return this;
    }
}
