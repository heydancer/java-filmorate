package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService extends GlobalService<Film> {

    public FilmService(InMemoryStorage<Film> inMemoryStorage) {
        super(inMemoryStorage);
    }

    public Film addLike(int filmId, int userId) {
        if (!getSetLikesById(filmId).add(userId)) {
            log.warn("Impossible to addLike. User with id: {} liked it before", userId);
            throw new NotFoundException("Add like error");
        }

        return inMemoryStorage.getData(filmId);
    }

    public Film removeLike(int filmId, int userId) {
        if (!getSetLikesById(filmId).remove(userId)) {
            log.warn("Impossible to removeLike. User with {} did not leave a like", userId);
            throw new NotFoundException("Remove like error");
        }

        return inMemoryStorage.getData(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = inMemoryStorage.getAll();

        return popularFilms
                .stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public boolean validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Release date cannot be earlier than December 28, 1895");
            throw new ValidationException("Failed to validate on the release date");
        }

        if (film.getId() == 0) {
            film.setId(inMemoryStorage.getNextId());
        }

        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }

        return true;
    }

    private Set<Integer> getSetLikesById(int filmId) {
        Film film = inMemoryStorage.getData(filmId);

        return film.getLikes();
    }
}
