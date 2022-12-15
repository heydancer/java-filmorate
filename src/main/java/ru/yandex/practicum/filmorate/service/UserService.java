package ru.yandex.practicum.filmorate.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage storage) {
        this.userStorage = storage;
    }

    public User create(User user) {
        return userStorage.add(user);
    }

    public User update(User user, int userId) {
        userStorage.checkUserInDb(userId);

        return userStorage.update(user, userId);
    }

    public User addFriend(int userId, int friendId) {
        userStorage.checkUserInDb(friendId);

        return userStorage.addFriend(userId, friendId);
    }

    public User getById(int userId) {
        userStorage.checkUserInDb(userId);

        return userStorage.getData(userId);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public List<User> getFriends(int userId) {
        return userStorage.getFriendList(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        userStorage.checkUserInDb(userId);
        userStorage.checkUserInDb(otherId);

        return userStorage.getCommonFriends(userId, otherId);
    }

    public User removeUser(int userId) {
        userStorage.checkUserInDb(userId);

        return userStorage.removeData(userId);
    }

    public User removeFriend(int userId, int friendId) {
        userStorage.checkUserInDb(userId);
        userStorage.checkUserInDb(friendId);

        return userStorage.removeFriend(userId, friendId);
    }

    public void validate(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
    }
}
