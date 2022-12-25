package ru.practicum.shareit.abstracts;

import java.util.List;
import java.util.Optional;

public interface GeneralStorage<T> {
    Optional<T> getEntityById(long id);
    List<T> getAll();
    T addEntity(T entity);
    T changeEntity(T entity);
    void deleteEntity(long id);
}
