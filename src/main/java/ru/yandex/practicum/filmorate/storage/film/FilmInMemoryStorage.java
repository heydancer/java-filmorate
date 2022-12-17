package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Component
public class FilmInMemoryStorage implements FilmStorage {

    private final Map<Integer, Film> dataMap = new HashMap<>();
    private int nextId = 0;

    public int getNextId() {
        return ++nextId;
    }

    @Override
    public Film add(Film film) {
        if (film.getId() == 0) {
            film.setId(getNextId());
        }

        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        dataMap.put(nextId, film);

        return film;
    }

    @Override
    public Film update(Film film, int filmId) {
        checkFilm(filmId);
        dataMap.put(nextId, film);

        return film;
    }

    @Override
    public void removeData(int filmId) {
        checkFilm(filmId);
        dataMap.remove(filmId);
    }

    @Override
    public Film getData(int filmId) {
        checkFilm(filmId);

        return dataMap.get(filmId);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(dataMap.values());
    }

    @Override
    public Film addLike(int filmId, int userId) {
        if (!getSetLikesById(filmId).add(userId)) {
            log.warn("Impossible to addLike. User with id: {} liked it before", userId);
            throw new NotFoundException("Add like error");
        }

        return getData(filmId);
    }

    @Override
    public List<Film> getPopular(int amount) {
        List<Film> popularFilms = getAll();

        return popularFilms
                .stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(amount)
                .collect(Collectors.toList());
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        if (!getSetLikesById(filmId).remove(userId)) {
            log.warn("Impossible to removeLike. User with {} did not leave a like", userId);
            throw new NotFoundException("Remove like error");
        }

        return getData(filmId);
    }

    @Override
    public void checkFilm(int filmId) {
        if (!dataMap.containsKey(filmId)) {
            log.warn("ID - {} does not exist", filmId);
            throw new NotFoundException("Film with ID: " + filmId + " not found");
        }
    }

    private Set<Integer> getSetLikesById(int filmId) {
        return getData(filmId).getLikes();
    }
}
