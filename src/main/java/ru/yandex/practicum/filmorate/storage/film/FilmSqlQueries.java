package ru.yandex.practicum.filmorate.storage.film;

final class FilmSqlQueries {
    static final String ADD_FILM = "INSERT INTO films(name,description, release_date, duration, mpa_id) " +
            "VALUES(?,?,?,?,?)";
    static final String ADD_LIKE = "INSERT INTO likes(user_id, film_id) " +
            "VALUES(?,?)";
    static final String ADD_GENRES = "INSERT INTO film_genres(film_id, genre_id) " +
            "VALUES(?,?)";
    static final String REMOVE_FILM = "DELETE FROM films " +
            "WHERE film_id = ?";
    static final String REMOVE_GENRES = "DELETE FROM film_genres " +
            "WHERE film_id = ?";
    static final String REMOVE_LIKE = "DELETE FROM likes " +
            "WHERE user_id = ? " +
            "AND film_id = ?";
    static final String UPDATE_FILM = "UPDATE films " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
            "WHERE film_id = ?";
    static final String GET_FILM = "SELECT * " +
            "FROM films AS f JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "WHERE f.film_id = ?";
    static final String GET_FILMS = "SELECT * " +
            "FROM films AS f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id";
    static final String GET_POPULAR = "SELECT " +
            "films.film_id, " +
            "films.name, " +
            "films.description, " +
            "films.release_date, " +
            "films.duration," +
            "mpa.mpa_id, " +
            "mpa.mpa_name, " +
            "COUNT(likes.film_id)" +
            "FROM films " +
            "JOIN mpa ON films.mpa_id = mpa.mpa_id " +
            "LEFT JOIN likes ON films.film_id = likes.film_id " +
            "GROUP BY films.film_id, films.name, films.description, films.release_date, films.duration " +
            "ORDER BY COUNT(likes.film_id) DESC " +
            "LIMIT ?";
}
