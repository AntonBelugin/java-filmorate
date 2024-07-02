package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Component
public class UserController {
    private final UserService userService;
    private final ValidateService validateService;

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("==> POST /users");
        validateService.validateUser(user);
        User newUser = userService.create(user);
        log.info("<== POST /users {}", newUser.getId() + newUser.getName());
        return newUser;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("==> PUT /users");
        validateService.validateUpdateUser(user);
        User upUser = userService.update(user);
        log.info("<== PUT /users {}", upUser.getId() + upUser.getName());
        return upUser;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id,  @PathVariable long friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id,  @PathVariable long friendId) {
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable long id) {
        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.findCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable long id) {
        log.info("==> PUT /users/{id}");
        return userService.findById(id);
    }

}


