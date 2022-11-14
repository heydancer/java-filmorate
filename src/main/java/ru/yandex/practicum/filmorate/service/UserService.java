package ru.yandex.practicum.filmorate.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class UserService extends GlobalService<User> {

    @Override
    public boolean validate(User user) {
        boolean check = true;
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
            check = false;
        }

        return check;
    }
}
