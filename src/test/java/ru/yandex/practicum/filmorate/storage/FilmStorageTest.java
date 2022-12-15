package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageTest {

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final User testUser = User.builder()
            .name("Test user")
            .login("tester")
            .email("testuser@yandex.ru")
            .birthday(LocalDate.of(1994, 4, 16))
            .build();
    private final Film testFilm = Film.builder()
            .name("Test film")
            .description("Test description")
            .releaseDate(LocalDate.of(2022, 12, 15))
            .duration(180)
            .mpa(new Mpa(4, "R"))
            .genres(null)
            .build();;
    private final Film testFilm2 = Film.builder()
            .name("Test film2")
            .description("Test description2")
            .releaseDate(LocalDate.of(2000, 10, 10))
            .duration(180)
            .mpa(new Mpa(4, "R"))
            .genres(null)
            .build();;


    @Test
    public void shouldCreateFilm() {
        Film film = filmDbStorage.add(testFilm);

        assertThat(film).isNotNull();
        assertThat(film.getId()).isEqualTo(1);
        assertThat(film.getName()).isEqualTo("Test film");
        assertThat(film.getDescription()).isEqualTo("Test description");
    }

    @Test
    public void shouldUpdateFilm() {
        Film film = filmDbStorage.add(testFilm);
        Film updatedFilm = filmDbStorage.update(testFilm2, film.getId());

        assertThat(updatedFilm).isNotNull();
        assertThat(updatedFilm.getId()).isEqualTo(1);
        assertThat(updatedFilm.getName()).isEqualTo("Test film2");
        assertThat(updatedFilm.getDescription()).isEqualTo("Test description2");
    }

    @Test
    public void shouldRemoveFilmByIdAndReturnNull() {
        Film film = filmDbStorage.add(testFilm);
        Film film2 = filmDbStorage.add(testFilm2);

        filmDbStorage.removeData(film.getId());
        filmDbStorage.removeData(film2.getId());

        assertThat(filmDbStorage.getData(film.getId())).isNull();
        assertThat(filmDbStorage.getAll()).hasSize(0);
    }

    @Test
    void shouldReturnFilmById() {
        Film film = filmDbStorage.add(testFilm);

        assertThat(filmDbStorage.getData(film.getId())).isNotNull();
        assertThat(filmDbStorage.getData(film.getId())).isEqualTo(film);
    }

    @Test
    void shouldReturnNullWithANonExistentId() {
        assertThat(filmDbStorage.getData(9999)).isNull();
    }

    @Test
    public void shouldReturnAllFilms() {
        filmDbStorage.add(testFilm);
        filmDbStorage.add(testFilm2);

        assertThat(filmDbStorage.getAll()).hasSize(2);
    }

    @Test
    public void shouldReturnEmptyListFilms() {
        assertThat(filmDbStorage.getAll()).isEmpty();
    }

    @Test
    public void shouldReturnPopularFilms() {
        Film filmWithLike = filmDbStorage.add(testFilm);
        Film filmWithoutLike = filmDbStorage.add(testFilm2);

        User user = userDbStorage.add(testUser);

        filmDbStorage.addLike(filmWithLike.getId(), user.getId());

        assertThat(filmDbStorage.getPopular(2)).hasSize(2);
        assertThat(filmDbStorage.getPopular(2).get(0)).isEqualTo(filmWithLike);
        assertThat(filmDbStorage.getPopular(2).get(1)).isEqualTo(filmWithoutLike);
    }
}
