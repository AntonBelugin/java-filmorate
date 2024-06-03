package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public void create(User user) {
        userStorage.save(user);
    }

    public void update(User upUser) {
        userStorage.testUser(upUser.getId());
        userStorage.update(upUser);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriends(long userId, long friendId) {
        userStorage.testUser(userId);
        userStorage.testUser(friendId);
        userStorage.addFriends(userId, friendId);
    }

    public void deleteFriends(long userId, long friendId) {
        userStorage.testUser(userId);
        userStorage.testUser(friendId);
        if (userStorage.getUserFriendsIds().containsKey(userId) &&
                userStorage.getUserFriendsIds().get(userId).contains(friendId)) {
            userStorage.deleteFriends(userId, friendId);
        }
    }

    public Collection<User> findFriends(long userId) {
        userStorage.testUser(userId);
        return userStorage.findFriends(userId);
    }

    public Collection<User> findCommonFriends(long userId, long otherId) {
        userStorage.testUser(userId);
        userStorage.testUser(otherId);
        return userStorage.findCommonFriends(userId, otherId);
    }

}
