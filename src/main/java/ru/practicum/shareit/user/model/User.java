package ru.practicum.shareit.user.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Transient
    private Set<Item> items;

    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public User merge(User other) {
        if (other.name != null) name = other.name;
        if (other.email != null) email = other.email;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
