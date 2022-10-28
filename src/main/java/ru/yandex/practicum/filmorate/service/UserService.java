package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements GlobalService<User> {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int nextId = 0;

    @Override
    public User create(User user) {
        user.setId(++nextId);
        userMap.put(nextId, user);
        return user;
    }

    @Override
    public User update(User user) throws ValidationException{
        if (!userMap.containsKey(user.getId())) {
            throw new ValidationException("It's impossible to update USER, this id does not exist");
        }

        userMap.put(nextId, user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }
}
