package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface GeneralStorage<T> {
    T add(final T data);

    T update(final T data, final int id);

    T removeData(final int id);

    T getData(final int id);

    List<T> getAll();
}
