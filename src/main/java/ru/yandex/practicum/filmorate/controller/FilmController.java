package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {

    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Film is added: {}", film);
        filmService.validate(film);

        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Film is updated: {}", film);
        filmService.validate(film);

        return filmService.update(film, film.getId());

    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable int id, @PathVariable @Positive int userId) {
        log.info("Like the Film with id: {}", id);

        return filmService.addLike(id, userId);

    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("Film with id: {} getting", id);

        return filmService.getById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") @Positive int count) {
        log.info("The list of popular films returns");

        return filmService.getPopularFilms(count);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("The list of all films returns");

        return filmService.getAll();
    }

    @DeleteMapping("/{id}")
    public Film removeFilm(@PathVariable int id) {
        log.info("Film with id: {} removed", id);

        return filmService.removeById(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeFilmLikes(@PathVariable int id, @PathVariable int userId) {
        log.info("Film's like with id: {} removed", id);

        return filmService.removeLike(id, userId);
    }
}
