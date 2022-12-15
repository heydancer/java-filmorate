package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getById(int genreId) {
        try {
            return jdbcTemplate.queryForObject(GenreSqlQueries.GET_GENRE, this::mapRow, genreId);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(GenreSqlQueries.GET_GENRES, this::mapRow);
    }

    @Override
    public List<Genre> getAllByFilmId(int filmId) {
        return jdbcTemplate.query(GenreSqlQueries.GET_GENRES_BY_FILM_ID, this::mapRow, filmId);
    }

    @Override
    public void checkGenreInDb(int genreId) {
        if (getById(genreId) == null) {
            throw new NotFoundException("Genre with ID: " + genreId + " not found");
        }
    }

    private Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }
}
