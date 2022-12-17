package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface GeneralStorage<T> {
    T add(final T data);

    T update(final T data, final int id);

    void removeData(final int id);

    T getData(final int id);

    List<T> getAll();
}
