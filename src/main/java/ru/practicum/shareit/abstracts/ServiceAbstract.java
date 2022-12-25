package ru.practicum.shareit.abstracts;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class ServiceAbstract<T, K extends GeneralStorage<T>> implements GeneralService<T> {

    protected K storage;

    @Override
    public T getEntityById(long id) {
        Optional<T> optionalT = storage.getEntityById(id);
        return optionalT.orElseThrow(
                () -> new NoSuchElementException(optionalT.getClass().getName() + " with id = " + id + " not found")
        );
    }

    @Override
    public List<T> getAll() {
        return storage.getAll();
    }

    @Override
    public T addEntity(T entity) {
        if (!isValidEntity(entity)) throw new NoSuchElementException("entity to add is not valid");
        return storage.addEntity(entity);
    }

    @Override
    public T changeEntity(T entity) {
        if (!exists(entity)) throw new NoSuchElementException("entity to change is not found");
        if (!isValidEntity(entity)) throw new NoSuchElementException("entity to change is not valid");
        return storage.changeEntity(entity);
    }

    @Override
    public void deleteEntity(long id) {
        if (!exists(id)) throw new NoSuchElementException("entity to delete is not found");
        storage.deleteEntity(id);
    }

    @Override
    public boolean isValidEntity(T entity) {
        return true;
    }

    @Override
    public boolean exists(T entity) {
        return true;
    }

    @Override
    public boolean exists(long id) {
        getEntityById(id);
        return true;
    }
}
