package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage storage, UserStorage userStorage) {
        this.filmStorage = storage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film, int filmId) {
        filmStorage.checkFilmInDb(filmId);

        return filmStorage.update(film, filmId);
    }

    public Film addLike(int filmId, int userId) {
        filmStorage.checkFilmInDb(filmId);
        userStorage.checkUserInDb(userId);

        return filmStorage.addLike(filmId, userId);

    }

    public Film getById(int filmId) {
        filmStorage.checkFilmInDb(filmId);

        return filmStorage.getData(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopular(count);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film removeById(int filmId) {
        filmStorage.checkFilmInDb(filmId);

        return filmStorage.removeData(filmId);
    }

    public Film removeLike(int filmId, int userId) {
        filmStorage.checkFilmInDb(filmId);
        userStorage.checkUserInDb(userId);

        return filmStorage.removeLike(filmId, userId);
    }

    public boolean validate(Film film) {
        if (film == null) {
            log.warn("Film cannot be null");
            throw new ValidationException("Failed to validate film's body is empty");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Release date cannot be earlier than December 28, 1895");
            throw new ValidationException("Failed to validate on the release date");
        }

        return true;
    }
}
