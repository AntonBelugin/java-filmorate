package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.UserStorage;


@Service
public class UserService {
    private UserStorage userStorage;

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

    public void findFriends(long userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Неверный Id пользователя");
        }
        userStorage.findFriends(userId);
    };

    public void findCommonFriends(long userId, long otherId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Неверный Id пользователя");
        }
        if (!userStorage.getUsers().containsKey(otherId)) {
            throw new NotFoundException("Неверный Id друга");
        }
        userStorage.findCommonFriends(userId, otherId);
    };



}
