package ru.yandex.practicum.filmorate.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService extends GlobalService<User> {
    public UserService(InMemoryStorage<User> inMemoryStorage) {
        super(inMemoryStorage);
    }

    public User addFriend(int id, int friendId) {
        User user = inMemoryStorage.getData(id);
        User friend = inMemoryStorage.getData(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        return user;
    }

    public User removeFriend(int id, int friendId) {
        User user = inMemoryStorage.getData(id);
        User friend = inMemoryStorage.getData(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);

        return friend;
    }

    public List<User> getFriends(int id) {

        List<User> friendsList = new ArrayList<>();

        User user = inMemoryStorage.getData(id);

        Set<Integer> friends = user.getFriends();

        for (Integer friendId : friends) {
            friendsList.add(inMemoryStorage.getData(friendId));
        }

        return friendsList;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        User user = inMemoryStorage.getData(id);
        User otherUser = inMemoryStorage.getData(otherId);

        List<User> friendsList = new ArrayList<>();

        for (int friendId : user.getFriends()) {
            for (int anotherId : otherUser.getFriends()) {
                if (friendId == anotherId) {
                    friendsList.add(inMemoryStorage.getData(friendId));
                }
            }
        }

        return friendsList;
    }

    @Override
    public boolean validate(User user) {
        boolean check = true;
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
            check = false;
        }

        if (user.getId() == 0) {
            user.setId(inMemoryStorage.getNextId());
        }

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }

        return check;
    }
}
