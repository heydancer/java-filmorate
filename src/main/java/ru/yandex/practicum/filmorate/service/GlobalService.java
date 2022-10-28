package ru.yandex.practicum.filmorate.service;

import java.util.List;

public interface GlobalService<T> {

    T create(T t);

    T update(T t);

    List<T> getAll();


}
