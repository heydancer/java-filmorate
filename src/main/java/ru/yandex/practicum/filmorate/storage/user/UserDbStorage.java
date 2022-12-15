package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    UserSqlQueries.ADD_USER, new String[]{"user_id"});
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
            return preparedStatement;
        }, keyHolder);

        int userId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        user.setId(userId);

        return getData(userId);
    }

    @Override
    public User update(User user, int userId) {
        if (getData(userId) == null) {
            return null;
        }

        jdbcTemplate.update(UserSqlQueries.UPDATE_USER, user.getName(), user.getEmail(),
                user.getLogin(), user.getBirthday(), userId);

        user.setId(userId);

        return getData(userId);
    }

    @Override
    public User removeData(int userId) {
        User removedUser = getData(userId);
        jdbcTemplate.update(UserSqlQueries.REMOVE_USER, userId);

        return removedUser;
    }

    @Override
    public User getData(int userId) {
        try {
            return jdbcTemplate.queryForObject(UserSqlQueries.GET_USER, this::mapRow, userId);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(UserSqlQueries.GET_USERS, this::mapRow);
    }

    @Override
    public User addFriend(int userId, int friendId) {
        SqlRowSet mutualFriendship = jdbcTemplate.queryForRowSet(
                UserSqlQueries.CHECK_MUTUAL_FRIENDSHIP, friendId, userId);

        if (mutualFriendship.next()) {
            jdbcTemplate.update(UserSqlQueries.UPDATE_FRIEND_STATUS, true, friendId, userId, false);
            jdbcTemplate.update(UserSqlQueries.ADD_FRIEND, userId, friendId, true);
            log.info("User with id: {} accepted friend request of User with id: {}", friendId, userId);
        } else {
            jdbcTemplate.update(UserSqlQueries.ADD_FRIEND, userId, friendId, false);
            log.info("User with id: {} subscribed to User with id: {}", userId, friendId);
        }

        return getData(friendId);
    }

    @Override
    public List<User> getFriendList(int userId) {
        return jdbcTemplate.query(UserSqlQueries.GET_FRIENDS, this::mapRow, userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        return jdbcTemplate.query(UserSqlQueries.GET_COMMON_FRIENDS, this::mapRow, userId, otherId);
    }

    @Override
    public User removeFriend(int userId, int friendId) {
        SqlRowSet mutualFriendship = jdbcTemplate.queryForRowSet(
                UserSqlQueries.CHECK_MUTUAL_FRIENDSHIP, friendId, userId);

        if (mutualFriendship.next()) {
            jdbcTemplate.update(UserSqlQueries.UPDATE_FRIEND_STATUS, false, friendId, userId, true);
            jdbcTemplate.update(UserSqlQueries.REMOVE_FRIEND, userId, friendId);
            log.info("User with id: {} removed from friends User with id: {}", userId, friendId);
        } else {
            jdbcTemplate.update(UserSqlQueries.REMOVE_FRIEND, userId, friendId);
            log.info("User with id: {} unsubscribed to User with id: {}", userId, friendId);
        }

        return getData(friendId);
    }

    @Override
    public void checkUserInDb(int userId) {
        if (getData(userId) == null) {
            throw new NotFoundException("User with ID: " + userId + " not found");
        }
    }

    private User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getTimestamp("birthday").toLocalDateTime().toLocalDate())
                .build();
    }
}
