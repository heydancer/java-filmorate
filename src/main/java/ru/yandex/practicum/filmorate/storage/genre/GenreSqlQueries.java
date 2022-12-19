package ru.yandex.practicum.filmorate.storage.genre;

final class GenreSqlQueries {
    static final String GET_GENRES = "SELECT * " +
            "FROM genres";
    static final String GET_GENRE = "SELECT * " +
            "FROM genres " +
            "WHERE genre_id = ?";
    static final String GET_GENRES_BY_FILM_ID = "SELECT g.genre_id, genre_name " +
            "FROM genres AS g " +
            "JOIN film_genres AS fg ON g.genre_id = fg.genre_id " +
            "WHERE film_id = ?";
}
