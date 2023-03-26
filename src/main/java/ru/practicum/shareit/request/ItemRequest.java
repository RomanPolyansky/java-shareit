package ru.practicum.shareit.request;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TODO Sprint add-item-requests.
 */

@Entity
@Data
@Table(name = "item_requests")
public class ItemRequest {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;
    @JoinColumn(name = "requester_id")
    @ManyToOne
    private User requester;
    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;
    @Transient
    private List<Item> items = new ArrayList<>();
    public ItemRequest() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequest that = (ItemRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
