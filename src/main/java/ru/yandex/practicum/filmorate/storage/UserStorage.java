package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserStorage {

    void save(User user);

    void delete(User user);

    void update(User user);

    Collection<User> findAll();

    void addFriends(long userId, long friendId);

    void deleteFriends(long userId, long friendId);

    Collection<User> findFriends(long userId);

    Collection<User> findCommonFriends(long userId, long otherId);

    Map<Long, User> getUsers();

    HashMap<Long, Set<Long>> getUserFriendsIds();

    User findById(long userId);
}
