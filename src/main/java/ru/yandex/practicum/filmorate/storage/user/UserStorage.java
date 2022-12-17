package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.GeneralStorage;

import java.util.List;

public interface UserStorage extends GeneralStorage<User> {

    User addFriend(final int userId, final int friendId);

    List<User> getFriendList(final int userId);

    List<User> getCommonFriends(final int id, final int otherId);

    User removeFriend(final int userId, final int friendId);

    void checkUser(final int userId);
}
