package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    //private Map<Long, User> users = new HashMap<>();
    private ValidateService validateService;
    private Long currentId = 0L;
    private UserStorage userStorage;
    @Autowired
    public UserController(ValidateService validateService, UserStorage userStorage, UserService userService) {
        this.validateService = validateService;
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("==> POST /users");
        validateService.validateUser(user);
        user.setId(getNextId());
        //users.put(user.getId(), user);
        userStorage.save(user);
        log.info("<== POST /users {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User upUser) {
        log.info("==> PUT /users");
        validateService.validateUpdateUser(upUser);
        if (!userStorage.getUsers().containsKey(upUser.getId())) {
            throw new NotFoundException("Неверный Id");
        }
        userStorage.update(upUser);
        log.info("<== PUT /users {}", upUser);
        return upUser;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("==> GET /users");
        log.info("<== GET /users {}", userStorage.findAll());
        return userStorage.findAll();
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ValidationException e) {
        return new ErrorResponse(
                "Ошибка с параметрами",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final NotFoundException e) {
        return new ErrorResponse(
                "Неправильный ввод данных",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final RuntimeException e) {
        return new ErrorResponse(
                "Внутренняя ошибка сервера",
                e.getMessage()
        );
    }

    private long getNextId() {
        return ++currentId;
    }

}


