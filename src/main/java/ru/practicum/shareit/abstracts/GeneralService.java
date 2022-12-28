package ru.practicum.shareit.abstracts;

import java.util.List;

public interface GeneralService<T> {
    T getEntityById(long id);
    List<T> getAll();
    T addEntity(T entity);
    T changeEntity(T entity);
    void deleteEntity(long id);

    boolean isValidEntity(T entity);

    boolean exists(T entity);

    boolean exists(long id);
}
