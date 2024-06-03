package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ValidateService validateService;
    private Long currentId = 0L;

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("==> POST /users");
        validateService.validateUser(user);
        user.setId(getNextId());
        userService.create(user);
        log.info("<== POST /users {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User upUser) {
        log.info("==> PUT /users");
        validateService.validateUpdateUser(upUser);
        userService.update(upUser);
        log.info("<== PUT /users {}", upUser);
        return upUser;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("==> GET /users");
        log.info("<== GET /users {}", userService.findAll());
        return userService.findAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id,  @PathVariable long friendId) {
        log.info("==> PUT /users/{id}/friends/{friendId}");
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id,  @PathVariable long friendId) {
        log.info("==> DELETE /users/{id}/friends/{friendId}");
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable long id) {
        log.info("==> GET /users/{id}/friends");
        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("==> GET /users/{id}/friends/common/{otherId}");
        return userService.findCommonFriends(id, otherId);
    }

    private long getNextId() {
        return ++currentId;
    }

}


