package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import java.util.Collection;
import java.util.Optional;


@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private UserStorage userStorage;
    private FriendshipDbStorage friendshipDbStorage;

   @Autowired
    public UserService(UserStorage userStorage, FriendshipDbStorage friendShip) {
        this.userStorage = userStorage;
        this.friendshipDbStorage = friendShip;
    }

    public User create(User user) {
        return userStorage.save(user);
    }

    public User update(User user) {
        testUser(user.getId());
        return userStorage.update(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriends(long userId, long friendId) {
        testUser(userId);
        testUser(friendId);
        friendshipDbStorage.add(userId, friendId);
    }

    public void deleteFriends(long userId, long friendId) {
        testUser(userId);
        testUser(friendId);
        friendshipDbStorage.delete(userId, friendId);
    }

    public Collection<User> findFriends(long userId) {
        testUser(userId);
        return friendshipDbStorage.find(userId);
    }

    public Collection<User> findCommonFriends(long userId, long otherId) {
        testUser(userId);
        testUser(otherId);
        return friendshipDbStorage.findCommon(userId, otherId);
    }

    private void testUser(long id) {
        if (userStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Пользователя с id " + id + " не существует");
        }
    }

    public User findById(long id) {
       Optional<User> user = userStorage.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            log.warn("Пользователь с id {} не найден", id);
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
    }

}