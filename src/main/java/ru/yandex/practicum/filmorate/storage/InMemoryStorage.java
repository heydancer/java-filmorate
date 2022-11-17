package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class InMemoryStorage<T> implements GeneralStorage<T> {

    private final Map<Integer, T> dataMap = new HashMap<>();

    private int nextId = 0;

    public int getNextId() {
        return ++nextId;
    }

    @Override
    public T add(final T data) {
        dataMap.put(nextId, data);
        return data;

    }

    @Override
    public T update(final T data, final int id) {
        if (!dataMap.containsKey(id)) {
            log.warn("Impossible to update. ID - {} does not exist", id);
            throw new NotFoundException("Update error");
        }
        dataMap.put(nextId, data);

        return data;
    }

    @Override
    public T removeData(final int id) {
        if (!dataMap.containsKey(id)) {
            log.warn("Impossible to remove. ID - {} does not exist", id);
            throw new NotFoundException("Remove error");
        }

        return dataMap.remove(id);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(dataMap.values());
    }

    @Override
    public T getData(int id) {
        if (!dataMap.containsKey(id)) {
            log.warn("Impossible to get Data. ID - {} does not exist", id);
            throw new NotFoundException("Get By ID error");
        }

        return dataMap.get(id);
    }
}
