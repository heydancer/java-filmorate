package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreStorageTest {

    private final GenreDbStorage genreDbStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void shouldReturnGenreById() {
        assertThat(genreDbStorage.getById(1).getName()).isEqualTo("Комедия");
        assertThat(genreDbStorage.getById(3).getName()).isEqualTo("Мультфильм");
    }

    @Test
    public void shouldReturnAllGenres() {
        assertThat(genreDbStorage.getAll()).isNotNull();
        assertThat(genreDbStorage.getAll()).hasSize(6);
    }

    @Test
    public void getAllByFilmId() {
        List<Genre> genres = new ArrayList<>();
        genres.add(genreDbStorage.getById(1));
        genres.add(genreDbStorage.getById(2));
        genres.add(genreDbStorage.getById(3));

        Film film = filmDbStorage.add(Film.builder()
                .name("Test film")
                .description("Test description")
                .releaseDate(LocalDate.of(2022, 12, 15))
                .duration(180)
                .mpa(new Mpa(4, "R"))
                .genres(genres)
                .build());

        assertThat(genreDbStorage.getAllByFilmId(film.getId())).isNotNull();
        assertThat(genreDbStorage.getAllByFilmId(film.getId())).hasSize(3);
    }
}
