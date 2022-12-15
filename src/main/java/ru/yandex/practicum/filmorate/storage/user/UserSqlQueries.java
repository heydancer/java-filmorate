package ru.yandex.practicum.filmorate.storage.user;

final class UserSqlQueries {
    static final String ADD_USER = "INSERT INTO users(name, email, login, birthday) " +
            "VALUES(?,?,?,?)";
    static final String ADD_FRIEND = "INSERT INTO friends(user_id, friend_id, status) " +
            "VALUES (?,?,?)";
    static final String REMOVE_USER = "DELETE FROM users " +
            "WHERE user_id = ?";
    static final String REMOVE_FRIEND = "DELETE FROM friends " +
            "WHERE user_id = ? " +
            "AND friend_id = ?";
    static final String UPDATE_USER = "UPDATE users " +
            "SET name = ?, email = ?, login = ?, birthday = ? " +
            "WHERE user_id = ?";

    static final String UPDATE_FRIEND_STATUS = "UPDATE friends " +
            "SET status = ? " +
            "WHERE user_id = ? " +
            "AND friend_id = ? " +
            "AND status = ?";
    static final String CHECK_MUTUAL_FRIENDSHIP = "SELECT * " +
            "FROM friends " +
            "WHERE user_id = ? " +
            "AND friend_id = ?";
    static final String GET_USERS = "SELECT * " +
            "FROM users";
    static final String GET_USER = "SELECT * " +
            "FROM users " +
            "WHERE user_id = ?";
    static final String GET_FRIENDS = "SELECT * " +
            "FROM users AS u " +
            "LEFT OUTER JOIN friends AS f ON u.user_id = f.friend_id " +
            "WHERE f.user_id = ?";
    static final String GET_COMMON_FRIENDS = "SELECT * " +
            "FROM users AS u " +
            "LEFT OUTER JOIN friends AS f ON u.user_id = f.friend_id " +
            "WHERE f.user_id = ? " +
            "AND f.friend_id IN (" +
            "SELECT friend_id " +
            "FROM friends AS f " +
            "LEFT OUTER JOIN users AS u ON u.user_id = f.friend_id " +
            "WHERE f.user_id = ?)";
}
