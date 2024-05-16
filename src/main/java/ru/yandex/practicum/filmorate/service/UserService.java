package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;


@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(long userId, long friendId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Неверный Id пользователя");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new NotFoundException("Неверный Id друга");
        }
        userStorage.addFriends(userId, friendId);
    };

    public void deleteFriends(long userId, long friendId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Неверный Id пользователя");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new NotFoundException("Неверный Id друга");
        }
        userStorage.deleteFriends(userId, friendId);
    };

    public Collection<User> findFriends(long userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Неверный Id пользователя");
        }
        return userStorage.findFriends(userId);
    };

    public Collection<User> findCommonFriends(long userId, long otherId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Неверный Id пользователя");
        }
        if (!userStorage.getUsers().containsKey(otherId)) {
            throw new NotFoundException("Неверный Id друга");
        }
        return userStorage.findCommonFriends(userId, otherId);
    };



}
