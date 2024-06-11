package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
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
    private Long currentId = 0L;
  //  private final JdbcTemplate jdbc;
   // private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, username = ?, birthday = ? WHERE id = ?";

    @PostMapping
    public User create(@RequestBody User user) {
     /*   log.info("==> POST /users");
        //String filmsTableQuery = "SELECT COUNT(*) FROM users";
       // System.out.print("result");
       // System.out.print("result" + jdbc.queryForObject(filmsTableQuery, Integer.class) + "  result");
        //String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
     //   jdbc.update(query,
      //          "user", "us@mail.ru", "123");*/
        validateService.validateUser(user);
        //user.setId(getNextId());
        User newUser = userService.create(user);
        log.info("<== POST /users {}", newUser);
        return newUser;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("==> PUT /films");
        System.out.println();
        System.out.println(user);
        System.out.println("validate test");
        validateService.validateUpdateUser(user);
       // System.out.println();
        System.out.println();
        System.out.println(user);
        System.out.println();
        System.out.println("validate ok");

     /*   jdbc.update(
                UPDATE_QUERY,
                upUser.getEmail(),
                upUser.getLogin(),
                upUser.getName(),
                upUser.getBirthday(),
                upUser.getId()
        );*/
        User upUser = userService.update(user);
      /*  System.out.println();
        System.out.println(123);*/
        System.out.println();
        System.out.println(upUser);
        System.out.println("posle update");
      //  log.info("<== PUT /users {}", upUser);
        return upUser;
    }

    @GetMapping
    public Collection<User> findAll() {
      //  log.info("==> GET /users");
      //  log.info("<== GET /users {}", userService.findAll());
        return userService.findAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id,  @PathVariable long friendId) {
      //  log.info("==> PUT /users/{id}/friends/{friendId}");
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id,  @PathVariable long friendId) {
     //   log.info("==> DELETE /users/{id}/friends/{friendId}");
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable long id) {
     //   log.info("==> GET /users/{id}/friends");
        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable long id, @PathVariable long otherId) {
     //   log.info("==> GET /users/{id}/friends/common/{otherId}");
        return userService.findCommonFriends(id, otherId);
    }

    private long getNextId() {
        return ++currentId;
    }

}


