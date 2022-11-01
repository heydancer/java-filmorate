package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class GlobalService<T> {
    protected final Map<Integer, T> dataMap = new HashMap<>();
    private int nextId = 0;

    public int getNextId() {
        return ++nextId;
    }

    public T create(final T data) {
        dataMap.put(nextId, data);

        return data;
    }

    public T update(final T data, final int id) {
        if (!dataMap.containsKey(id)) {
            log.warn("Impossible to update. ID - {} does not exist", id);
            throw new ValidationException("Update error");
        }
        dataMap.put(nextId, data);

        return data;
    }

    public List<T> getAll() {
        return new ArrayList<>(dataMap.values());
    }

    public abstract void validate(final T data);
}
