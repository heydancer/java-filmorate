package ru.yandex.practicum.filmorate.storage.mpa;

final class MpaSqlQueries {
    static final String GET_ALL_MPA = "SELECT * " +
            "FROM mpa " +
            "ORDER BY mpa_id";
    static final String GET_MPA = "SELECT * " +
            "FROM mpa " +
            "WHERE mpa_id = ?";
}
