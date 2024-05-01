package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Long, User> users = new HashMap<>();
    private ValidateService validateService;
    private Long currentId = 0L;

    public UserController(ValidateService validateService) {
        this.validateService = validateService;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("==> POST /users");
        validateService.validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("<== POST /users {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User upUser) {
        log.info("==> PUT /users");
        validateService.validateUpdateUser(upUser);
        if (!users.containsKey(upUser.getId())) {
            throw new ValidationException("Неверный Id");
        }
        users.put(upUser.getId(), upUser);
        log.info("<== PUT /users {}", upUser);
        return upUser;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("==> GET /users");
        log.info("<== GET /users {}", users.values());
        return users.values();
    }

    private long getNextId() {
        return ++currentId;
    }

}


