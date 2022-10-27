package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Integer, User> userMap = new HashMap<>();
    private int userIdCounter = 0;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {

        if (userValidationCheck(user) && !userMap.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            user.setId(++userIdCounter);
            userMap.put(userIdCounter, user);
        }

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {

        if (userValidationCheck(user) && userMap.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            userMap.put(userIdCounter, user);

        } else {
            throw new ValidationException("It's impossible to update the user, the user with this id does not exist");
        }

        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    private boolean userValidationCheck(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Email cannot be null or empty");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Login cannot be null or empty");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday cannot be in the future");
        }

        return true;
    }
}

