package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Component
public class UserInMemoryStorage implements UserStorage {

    private final Map<Integer, User> dataMap = new HashMap<>();

    private int nextId = 0;

    public int getNextId() {
        return ++nextId;
    }

    @Override
    public User add(User user) {
        if (user.getId() == 0) {
            user.setId(getNextId());
        }

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        dataMap.put(nextId, user);

        return user;
    }

    @Override
    public User update(User user, int userId) {
        dataMap.put(nextId, user);

        return user;
    }

    @Override
    public void removeData(int userId) {
        dataMap.remove(userId);
    }

    @Override
    public User getData(int userId) {
        return dataMap.get(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(dataMap.values());
    }

    @Override
    public User addFriend(int userId, int friendId) {
        User user = getData(userId);
        User friend = getData(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        return user;
    }

    @Override
    public List<User> getFriendList(int userId) {
        User user = getData(userId);

        List<User> friendsList = new ArrayList<>();
        Set<Integer> friends = user.getFriends();

        for (Integer friendId : friends) {
            friendsList.add(getData(friendId));
        }

        return friendsList;
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        User user = getData(userId);
        User otherUser = getData(otherId);

        List<User> friendsList = new ArrayList<>();

        for (int friendId : user.getFriends()) {
            for (int anotherId : otherUser.getFriends()) {
                if (friendId == anotherId) {
                    friendsList.add(getData(friendId));
                }
            }
        }

        return friendsList;
    }

    @Override
    public User removeFriend(int userId, int friendId) {
        User user = getData(userId);
        User friend = getData(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        return friend;
    }

    @Override
    public void checkUser(int userId) {
        if (!dataMap.containsKey(userId)) {
            log.warn("ID - {} does not exist", userId);
            throw new NotFoundException("User with ID: " + userId + " not found");
        }
    }
}
