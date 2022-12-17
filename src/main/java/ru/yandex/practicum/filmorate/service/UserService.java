package ru.yandex.practicum.filmorate.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage) {
        this.userStorage = storage;
    }

    public User create(User user) {
        return userStorage.add(user);
    }

    public User update(User user, int userId) {
        userStorage.checkUser(userId);

        return userStorage.update(user, userId);
    }

    public User addFriend(int userId, int friendId) {
        userStorage.checkUser(friendId);

        return userStorage.addFriend(userId, friendId);
    }

    public User getById(int userId) {
        userStorage.checkUser(userId);

        return userStorage.getData(userId);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public List<User> getFriends(int userId) {
        return userStorage.getFriendList(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        userStorage.checkUser(userId);
        userStorage.checkUser(otherId);

        return userStorage.getCommonFriends(userId, otherId);
    }

    public void removeUser(int userId) {
        userStorage.checkUser(userId);
        userStorage.removeData(userId);
    }

    public User removeFriend(int userId, int friendId) {
        userStorage.checkUser(userId);
        userStorage.checkUser(friendId);

        return userStorage.removeFriend(userId, friendId);
    }

    public void validate(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
    }
}
