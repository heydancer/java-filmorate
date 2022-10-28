package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("User is added: {}", user);
        userNameValidationCheck(user);
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("User is updated: {}", user);
        userNameValidationCheck(user);
        return userService.update(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("The list of all users returns");
        return new ArrayList<>(userService.getAll());
    }

    private void userNameValidationCheck(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}

