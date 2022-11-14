package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
@Service
public class FilmService extends GlobalService<Film> {

    @Override
    public boolean validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Release date cannot be earlier than December 28, 1895");
            throw new ValidationException("Failed to validate on the release date");
        }

        return true;
    }
}
