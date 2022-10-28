package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Film is added: {}", film);
        filmValidationCheck(film);
        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Film is updated: {}", film);
        filmValidationCheck(film);
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("The list of all films returns");
        return filmService.getAll();
    }

    private void filmValidationCheck(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Release date cannot be earlier than December 28, 1895");
            throw new ValidationException("Failed to validate on the release date");
        }
    }
}
