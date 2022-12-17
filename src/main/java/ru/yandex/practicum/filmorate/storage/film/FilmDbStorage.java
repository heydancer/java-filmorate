package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    FilmSqlQueries.ADD_FILM, new String[]{"film_id"});
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setInt(4, film.getDuration());
            preparedStatement.setInt(5, film.getMpa().getId());
            return preparedStatement;
        }, keyHolder);

        int filmId = Objects.requireNonNull(keyHolder.getKey()).intValue();

        film.setId(filmId);
        updateGenres(film);

        return film;
    }

    @Override
    public Film update(Film film, int filmId) {
        jdbcTemplate.update(FilmSqlQueries.UPDATE_FILM, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), filmId);

        film.setId(filmId);
        updateGenres(film);

        return film;
    }

    @Override
    public void removeData(int filmId) {
        if (jdbcTemplate.update(FilmSqlQueries.REMOVE_FILM, filmId) == 0) {
            log.warn("ID - {} does not exist", filmId);
            throw new NotFoundException("Film with ID: " + filmId + " not found");
        }
    }

    @Override
    public Film getData(int filmId) {
        try {
            return jdbcTemplate.queryForObject(FilmSqlQueries.GET_FILM, this::mapRow, filmId);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(FilmSqlQueries.GET_FILMS, this::mapRow);
    }

    @Override
    public Film addLike(int filmId, int userId) {
        jdbcTemplate.update(FilmSqlQueries.ADD_LIKE, userId, filmId);

        return getData(filmId);
    }

    @Override
    public List<Film> getPopular(int amount) {
        return jdbcTemplate.query(FilmSqlQueries.GET_POPULAR, this::mapRow, amount);

    }

    @Override
    public Film removeLike(int filmId, int userId) {
        jdbcTemplate.update(FilmSqlQueries.REMOVE_LIKE, userId, filmId);

        return getData(filmId);
    }

    @Override
    public void checkFilm(int filmId) {
        if (getData(filmId) == null) {
            log.warn("ID - {} does not exist", filmId);
            throw new NotFoundException("Film with ID: " + filmId + " not found");
        }
    }

    private void updateGenres(Film film) {
        if (film.getGenres() != null) {
            jdbcTemplate.update(FilmSqlQueries.REMOVE_GENRES, film.getId());

            List<Integer> genresIds = film.getGenres().stream()
                    .map(Genre::getId)
                    .distinct()
                    .collect(Collectors.toList());

            for (Integer genreId : genresIds) {
                jdbcTemplate.update(FilmSqlQueries.ADD_GENRES, film.getId(), genreId);
            }
        }

        if (film.getGenres() != null && film.getGenres().isEmpty()) {
            jdbcTemplate.update(FilmSqlQueries.REMOVE_GENRES, film.getId());
        }
    }

    private Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa filmMpa = Mpa.builder()
                .id(resultSet.getInt("mpa.mpa_id"))
                .name(resultSet.getString("mpa.mpa_name"))
                .build();

        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getTimestamp("release_date").toLocalDateTime().toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(filmMpa)
                .genres(genreStorage.getAllByFilmId(resultSet.getInt("film_id")))
                .build();
    }
}
