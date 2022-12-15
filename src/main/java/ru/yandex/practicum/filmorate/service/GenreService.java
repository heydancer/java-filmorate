package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage storage) {
        this.genreStorage = storage;
    }

    public List<Genre> findAll() {
        return genreStorage.getAll();
    }

    public Genre getById(int genreId) {
        genreStorage.checkGenreInDb(genreId);

        return genreStorage.getById(genreId);
    }
}
