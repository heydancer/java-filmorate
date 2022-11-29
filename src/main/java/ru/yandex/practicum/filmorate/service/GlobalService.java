package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;

import java.util.List;

@Slf4j
public abstract class GlobalService<T> {

    @Autowired
    protected InMemoryStorage<T> inMemoryStorage;

    public GlobalService(InMemoryStorage<T> inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }


    public T create(final T data) {
        return inMemoryStorage.add(data);
    }

    public T update(final T data, final int id) {
        return inMemoryStorage.update(data, id);
    }

    public T removeById(final int id) {
        return inMemoryStorage.removeData(id);
    }

    public T getById(int id) {
        return inMemoryStorage.getData(id);
    }

    public List<T> getAll() {
        return inMemoryStorage.getAll();
    }

    public abstract boolean validate(final T data);

}
