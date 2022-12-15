package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.GeneralStorage;

import java.util.List;

public interface FilmStorage extends GeneralStorage<Film> {
    Film addLike(final int filmId, final int userId);

    List<Film> getPopular(final int amount);

    Film removeLike(final int filmId, final int userId);

    void checkFilmInDb(final int filmId);
}
