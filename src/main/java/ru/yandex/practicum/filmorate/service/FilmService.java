package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilmService implements GlobalService<Film> {
    private final Map<Integer, Film> filmMap = new HashMap<>();
    private int nextId = 0;

    @Override
    public Film create(Film film) {
        film.setId(++nextId);
        filmMap.put(nextId, film);
        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException {
        if (!filmMap.containsKey(film.getId())) {
            throw new ValidationException("It's impossible to update FILM, this id does not exist");
        }

        filmMap.put(nextId, film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(filmMap.values());
    }
}
