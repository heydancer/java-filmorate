package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    Map<Integer, Film> filmMap = new HashMap<>();
    private int filmIdCounter = 0;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {

        if (filmValidationCheck(film) && !filmMap.containsKey(film.getId())) {
            film.setId(++filmIdCounter);
            filmMap.put(filmIdCounter, film);
        }

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {

        if (filmValidationCheck(film) && filmMap.containsKey(film.getId())) {
            filmMap.put(filmIdCounter, film);

        } else {
            throw new ValidationException("It's impossible to update the film, the film with this id does not exist");
        }

        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {

        return new ArrayList<>(filmMap.values());
    }
    private boolean filmValidationCheck(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Name cannot be null or empty");
        }

        if (film.getDescription() == null || film.getDescription().length() > 200) {
            throw new ValidationException("Description cannot be null or more than 200 characters");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Release date cannot be earlier than December 28, 1895");
        }

        if (film.getDuration() == null || film.getDuration() < 1) {
            throw new ValidationException("Duration cannot be null or negative");
        }

        return true;
    }
}
