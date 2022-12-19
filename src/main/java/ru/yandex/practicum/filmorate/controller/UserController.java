package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("User is added: {}", user);
        service.validate(user);

        return service.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("User is updated: {}", user);
        service.validate(user);

        return service.update(user, user.getId());
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Friend is adding : {}", friendId);

        return service.addFriend(id, friendId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("The list of all users returns");

        return service.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        log.info("User with id: {} getting", id);

        return service.getById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendList(@PathVariable int id) {
        log.info("Getting friends by User ID{}", id);

        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Getting common friends by User ID{}", otherId);

        return service.getCommonFriends(id, otherId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("User with id: {} removing", id);
        service.removeUser(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("User with id: {} removing from friends", id);

        return service.removeFriend(id, friendId);
    }
}

