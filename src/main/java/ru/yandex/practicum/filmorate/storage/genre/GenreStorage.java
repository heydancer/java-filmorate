package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;
import java.util.List;

public interface GenreStorage {

    Genre getById(final int genreId);

    List<Genre> getAllByFilmId(final int filmId);

    List<Genre> getAll();

    void checkGenreInDb(int genreId);
}
